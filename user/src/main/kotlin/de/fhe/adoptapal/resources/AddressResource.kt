package de.fhe.adoptapal.resources

import de.fhe.adoptapal.model.AddressEntity
import de.fhe.adoptapal.repository.AddressRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.time.LocalDateTime
import org.jboss.logging.Logger

@RequestScoped
@Path("/addresses")
class AddressResource {

    companion object {
        private val LOG: Logger = Logger.getLogger(AddressResource::class.java)
    }



    @Inject
    lateinit var addressRepository: AddressRepository

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): Response {
        return try {
            val addresses: List<AddressEntity> = addressRepository.listAll()
            if (addresses.isNotEmpty()) {
                Response.ok(addresses).build()
            } else {
                Response.status(Response.Status.NO_CONTENT).build()
            }
        } catch (e: Exception) {
            LOG.error("Failed to get all addresses", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") id: Long): Response {
        return addressRepository.findById(id)?.let {
            Response.ok(it).build()
        } ?: Response.status(Response.Status.NOT_FOUND).entity("Address with ID $id not found").build()
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createAddress(newAddress: AddressEntity): Response {
        return try {
            addressRepository.add(newAddress.street!!, newAddress.city!!, newAddress.postalCode!!)
            Response.status(Response.Status.CREATED).entity(newAddress).build()
        } catch (e: Exception) {
            LOG.error("Failed to create address", e)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
        }
    }
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateById(@PathParam("id") id: Long, updatedAddress: AddressEntity): Response {
        return addressRepository.findById(id)?.let { existingAddress ->
            try {
                existingAddress.apply {
                    street = updatedAddress.street.takeUnless { it.isNullOrEmpty() } ?: street
                    city = updatedAddress.city.takeUnless { it.isNullOrEmpty() } ?: city
                    postalCode = updatedAddress.postalCode.takeUnless { it.isNullOrEmpty() } ?: postalCode
                    lastChangeTimestamp = LocalDateTime.now()
                }
                addressRepository.updateAddress(existingAddress)
                Response.ok(existingAddress).build()
            } catch (e: Exception) {
                LOG.error("Failed to update address", e)
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
            }
        } ?: Response.status(Response.Status.NOT_FOUND).entity("Address with ID $id not found").build()
    }
    @DELETE
    @Path("/{id}")
    fun deleteById(@PathParam("id") id: Long): Response {
        return addressRepository.findById(id)?.let { addressEntity ->
            addressRepository.deleteAddress(id)
            Response.ok(addressEntity).build()
        } ?: Response.status(Response.Status.NOT_FOUND).entity("Address with ID $id not found").build()
    }
}