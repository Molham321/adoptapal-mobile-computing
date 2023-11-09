package de.fhe.ai.mc.resources

import de.fhe.ai.mc.*
import io.quarkus.grpc.GrpcClient
import io.smallrye.mutiny.Uni
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("grpc-service")
class Grpc2RestResource {

    @GrpcClient
    lateinit var contactService: ContactService

    @GET
    fun getAllContacts(): Uni<List<String>> {
        return contactService.getAll(Empty.newBuilder().build())
            .onItem().transform { it.contactsList }.map { list ->
                val stringList = ArrayList<String>()
                list.forEach {
                    stringList.add(it.toString())
                }
                stringList
            }
    }

}
