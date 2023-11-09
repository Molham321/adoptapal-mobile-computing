package de.fhe.ai.mc.resources

import de.fhe.ai.mc.*
import io.quarkus.grpc.GrpcClient
import io.smallrye.common.annotation.Blocking
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import java.time.Duration
import javax.ws.rs.GET
import javax.ws.rs.Path


@Path("grpc-hello")
class GrpcClientResource {

    @GrpcClient
    lateinit var greeter: Greeter

    @GrpcClient
    lateinit var streaming: MutinyStreamingGrpc.MutinyStreamingStub

    @GET
    @Path("/{name}")
    fun hello(name: String?): Uni<String?> {

        val requestPayload = HelloRequest.newBuilder().setName(name).build()

        return greeter
                .sayHello(requestPayload)
                .onItem().transform { it.message }
    }

    @GET
    @Path("source")
    fun source(): Multi<String> {
        return streaming.source(Empty.newBuilder().build())
            .onItem().transform { "$it.title " }
    }

    @GET
    @Path("sink")
    @Blocking
    fun sink(): Uni<String> {
        val input = Multi.createFrom().items(
            Item.newBuilder().setTitle("1").build(),
            Item.newBuilder().setTitle("2").build(),
            Item.newBuilder().setTitle("3").build()
        )
        return streaming.sink(input).onItem().ignore().andSwitchTo(Uni.createFrom().item("Success"))
    }

    @GET
    @Path("pipe")
    fun pipe(): Multi<String> {

        val input = Multi.createFrom().items(
            Item.newBuilder().setTitle("1").build(),
            Item.newBuilder().setTitle("2").build(),
            Item.newBuilder().setTitle("3").build()
        )

        return streaming.pipe(input).onItem().transform { it.title }
    }

}