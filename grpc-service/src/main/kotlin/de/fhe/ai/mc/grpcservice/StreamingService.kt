package de.fhe.ai.mc.grpcservice

import de.fhe.ai.mc.Empty
import de.fhe.ai.mc.Item
import de.fhe.ai.mc.Streaming
import io.quarkus.grpc.GrpcService
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni

@GrpcService
class StreamingService : Streaming {

    override fun source(request: Empty?): Multi<Item> {
        return Multi.createFrom().items(1,2,3,4,5)
            .onItem().transform { Item.newBuilder().setTitle("Item No. $it").build() }
    }

    override fun sink(request: Multi<Item>?): Uni<Empty> {

        return request?.invoke { item -> println(item.title) }?.collect()?.last()?.map { _ -> Empty.newBuilder().build() }!!
    }

    override fun pipe(request: Multi<Item>?): Multi<Item> {
        return request
            ?.invoke { item -> println(item.title) }
            ?.map { it.title }
            ?.onItem()?.transform { Item.newBuilder().setTitle("Item No. $it").build() }!!
    }
}