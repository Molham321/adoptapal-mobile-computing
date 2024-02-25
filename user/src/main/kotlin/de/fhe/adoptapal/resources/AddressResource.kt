package de.fhe.adoptapal.resources

import de.fhe.adoptapal.core.AddressBean
import de.fhe.adoptapal.core.mapExceptionToResponse
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger

@RequestScoped
@Path("/address")
class AddressResource {
    companion object {
        private val LOG: Logger = Logger.getLogger(AddressResource::class.java)
    }

    @Inject
    lateinit var addressBean: AddressBean

    @POST
    fun createAddress(request: CreateAddressRequest): Response {
        return try {
            Response.ok(addressBean.create( request.street, request.city, request.postalCode)).build()
        } catch (e: Exception) {
            LOG.error("failed to create address", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") id: Long): Response {
        return try {
            Response.ok(addressBean.get(id)).build()
        } catch (e: Exception) {
            LOG.error("failed to create address", e)
            mapExceptionToResponse(e)
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): Response {
        return try {
            val addresses = addressBean.getAll()
            if (addresses.isNotEmpty()) {
                Response.ok(addresses).build()
            } else {
                Response.status(Response.Status.NO_CONTENT).build()
            }
        } catch (e: Exception) {
            LOG.error("failed to get all addresses", e)
            mapExceptionToResponse(e)
        }
    }

    @PUT
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, request: UpdateAddressRequest): Response {
        return try {
            addressBean.update(id, request.newStreet, request.newCity, request.newPostalCode)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to update address with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }

    @DELETE
    @Path("/{id}")
    fun deleteById(@PathParam("id") id: Long): Response {
        return try {
            addressBean.delete(id)
            Response.ok().build()
        } catch (e: Exception) {
            LOG.error("failed to delete address with id `$id`", e)
            mapExceptionToResponse(e)
        }
    }
}