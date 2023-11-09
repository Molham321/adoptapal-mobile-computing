package de.fhe.ai.mc.resources

import de.fhe.ai.mc.data.PersonRepository
import de.fhe.ai.mc.model.Address
import de.fhe.ai.mc.model.Person
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import net.datafaker.Faker
import org.jboss.logging.Logger
import java.net.URI
import kotlin.random.Random


@Path("/persons")
class PersonResource {

    private val LOG: Logger = Logger.getLogger(PersonResource::class.java)

    @Inject
    lateinit var personRepository: PersonRepository

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createPerson(newPerson: Person): Uni<Response> {
        newPerson.addresses.forEach { address -> address.person = newPerson }

        return personRepository.persistAndFlush( newPerson )
            .map { persistedPerson ->
                    Response.created( URI("/persons/${persistedPerson.id}") ).build()
            }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(
        @QueryParam("sortby") @DefaultValue("id") sortBy: String,
        @QueryParam("filter") @DefaultValue("") filter: String
    ): Uni<List<Person>> =
        when(filter) {
            "" -> personRepository.listAll(Sort.by(sortBy).descending())
            else -> personRepository.findAllByCity(filter)
        }



    @DELETE
    @Path("/{personId:\\d}")
    fun deleteById(personId: Long): Uni<Response> {
        return personRepository.deleteById(personId)
            .map { deleted ->
                if (deleted)
                    Response.ok().build()
                else
                    Response.ok().status(Response.Status.NOT_FOUND).build()
            }
    }

    @GET
    @Path("/{personId:\\d}")
    @WithTransaction
    fun personById(personId: Long): Uni<Response> {
        return personRepository.findById(personId)
            .map { person ->
                if (person != null)
                    Response.ok(person).build()
                else
                    Response.ok().status(Response.Status.NOT_FOUND).build()
            }
    }

    @GET
    @Path("demo-data")
    fun getDemoData(
        @QueryParam("amount") @DefaultValue("10") amount: Long
    ): Multi<Person> =
        Multi.createFrom().emitter { emitter ->
            val faker = Faker()
            for(i in 1..amount) {
                var p = Person(faker.name().fullName())

                for(i in 0..Random.nextInt(2))
                    p.addresses.add(Address(p, faker.address().streetAddress(), faker.address().city(), faker.address().zipCode()))

                emitter.emit(p)
            }
            emitter.complete()
        }
}