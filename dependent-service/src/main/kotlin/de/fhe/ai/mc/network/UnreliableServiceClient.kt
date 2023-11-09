package de.fhe.ai.mc.network

import de.fhe.ai.mc.model.ValueWithIpAddress
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/api")
@RegisterRestClient(configKey = "unreliable-api")
interface UnreliableServiceClient {

    @GET
    @Path("unreliable-single-value-async")
    @Produces(MediaType.APPLICATION_JSON)
    fun unreliableSingleValueAsync(): Uni<ValueWithIpAddress>

    @GET
    @Path("multiple-values-async")
    @Produces(MediaType.APPLICATION_JSON)
    fun unreliableMultipleValuesAsync( @QueryParam("n") @DefaultValue("100") n: Int ): Uni<List<ValueWithIpAddress>>

}