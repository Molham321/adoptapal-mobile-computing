package de.fhe.ai.mc.grpcservice

import de.fhe.ai.mc.Greeter
import de.fhe.ai.mc.HelloReply
import de.fhe.ai.mc.HelloRequest
import io.quarkus.grpc.GrpcService
import io.smallrye.mutiny.Uni

@GrpcService
class HelloService : Greeter {

    override fun sayHello(request: HelloRequest?): Uni<HelloReply> {
        val reply = HelloReply.newBuilder().setMessage("Hello " + request?.name).build()
        return Uni.createFrom().item(reply)
    }

}