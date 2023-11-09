package de.fhe.ai.mc.resources

import de.fhe.ai.mc.core.getIpAddresses
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.smallrye.mutiny.Uni
import java.util.*
import java.util.function.Supplier
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import kotlin.random.Random


@Path("/api")
class UnreliableResource {

    @Inject
    lateinit var registry: MeterRegistry

    private val localIpAddress: String by lazy { getIpAddresses() }

    /**
     * A reliable endpoint for single entities
     */
    @GET
    @Path("single-value")
    @Produces(MediaType.APPLICATION_JSON)
    fun singleValue(): ValueWithIpAddress =
        ValueWithIpAddress("Time: " + System.currentTimeMillis(), localIpAddress)

    @GET
    @Path("single-value-async")
    @Produces(MediaType.APPLICATION_JSON)
    fun singleValueAsync(): Uni<ValueWithIpAddress> =
        Uni.createFrom().item( ValueWithIpAddress("Time: " + System.currentTimeMillis(), localIpAddress))

    /**
     * A reliable endpoint for a list of entities
     */
    @GET
    @Path("multiple-values")
    @Produces(MediaType.APPLICATION_JSON)
    fun multipleValues( @QueryParam("n") @DefaultValue("100") n: Int): List<ValueWithIpAddress> =
        (1..n).map {
            Thread.sleep(50)
            ValueWithIpAddress( "Time for $it: " + System.currentTimeMillis(), localIpAddress )
        }

    @GET
    @Path("multiple-values-async")
    @Produces(MediaType.APPLICATION_JSON)
    fun multipleValuesAsync( @QueryParam("n") @DefaultValue("100") n: Int ): Uni<List<ValueWithIpAddress>> {
        registry.counter("unreliable.service.multiple.values.async.requested.items").increment(n.toDouble())

        val timer = registry.timer("unreliable.service.multiple.values.async")

        return timer.record(Supplier {
            Uni.createFrom().item((1..n).map {
                Thread.sleep(Random.nextLong(50))
                ValueWithIpAddress( "Time for $it: " + System.currentTimeMillis(), localIpAddress )
            })
        })!!
    }

    /**
     * Enable histogram buckets for a specific timer
     */
    @Produces
    @ApplicationScoped
    fun enableHistogram(): MeterFilter? {
        return object : MeterFilter {
            override fun configure(id: Meter.Id, config: DistributionStatisticConfig): DistributionStatisticConfig? {
                return if (id.getName().startsWith("unreliable.service.multiple.values.async")) {
                    DistributionStatisticConfig.builder()
                        .percentiles(0.5, 0.95) // median and 95th percentile, not aggregable
                        .percentilesHistogram(true) // histogram buckets (e.g. prometheus histogram_quantile)
                        .build()
                        .merge(config)
                } else config
            }
        }
    }

    /**
     * Endpoint that will fail in roughly 50% of all requests.
     */
    @GET
    @Path("unreliable-single-value")
    @Produces(MediaType.APPLICATION_JSON)
    fun unreliableSingleValue(): ValueWithIpAddress {
        if (Random.nextBoolean()) throw RuntimeException("Resource failure on $localIpAddress}.")
        return singleValue()
    }

    @GET
    @Path("unreliable-single-value-async")
    @Produces(MediaType.APPLICATION_JSON)
    fun unreliableSingleValueAsync(): Uni<ValueWithIpAddress> {
        if (Random.nextBoolean()) throw RuntimeException("Resource failure on $localIpAddress}.")
        return singleValueAsync()
    }
}