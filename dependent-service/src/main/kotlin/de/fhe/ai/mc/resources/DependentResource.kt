package de.fhe.ai.mc.resources

import de.fhe.ai.mc.model.ValueWithIpAddress
import de.fhe.ai.mc.network.UnreliableServiceClient
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.time.Duration
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/api")
class DependentResource {

    @RestClient
    lateinit var unreliableServiceClient: UnreliableServiceClient

    @GET
    @Path("/unreliable-single-value-async")
    @Produces(MediaType.APPLICATION_JSON)
    fun singleValueAsync(): Uni<ValueWithIpAddress> =
        unreliableServiceClient.unreliableSingleValueAsync()
            .onFailure()
                .retry()
                    .withBackOff(Duration.ofMillis(200))
                    .withJitter(0.2)
                    .expireIn(Duration.ofSeconds(5).toMillis())
//                    .atMost(1)
            .onFailure().recoverWithItem(ValueWithIpAddress("Error", "i don't know"))

    @GET
    @Path("unreliable-multiple-values-async")
    @Produces(MediaType.APPLICATION_JSON)
    fun multipleValuesAsync( @QueryParam("n") @DefaultValue("100") n: Int): Uni<List<ValueWithIpAddress>> =
        unreliableServiceClient.unreliableMultipleValuesAsync( n )
            .ifNoItem().after(Duration.ofSeconds(3))
            .recoverWithItem(emptyList())
}