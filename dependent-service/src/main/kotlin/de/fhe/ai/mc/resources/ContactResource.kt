package de.fhe.ai.mc.resources

import de.fhe.ai.mc.model.Contact
import de.fhe.ai.mc.model.ContactRepository
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import java.net.URI
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("contacts")
class ContactResource {

    @Inject
    lateinit var repo: ContactRepository

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): Uni<MutableList<Contact>> = repo.listAll()

    @GET
    @Path("/{contactId:\\d}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById( contactId: Long ): Uni<Response> =
        repo.findById(contactId)
            .onItem().ifNotNull().transform { entity -> Response.ok(entity).build() }
            .onItem().ifNull().continueWith( Response.status(Response.Status.NOT_FOUND).build())

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun insert( contact: Contact ): Uni<Response> =
        repo.persistAndFlush( contact )
            .map { c -> Response.created( URI("/contacts/${c.id}") ).build() }

    @DELETE
    @Path("/{contactId:\\d}")
    @Produces(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    fun deleteById( contactId: Long ): Uni<Response> {
        return repo.deleteById(contactId)
            .map { deleted ->
                if (deleted)
                    Response.ok().build()
                else
                    Response.ok().status(Response.Status.NOT_FOUND).build()
            }
    }
}