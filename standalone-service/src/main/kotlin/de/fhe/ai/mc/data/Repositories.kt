package de.fhe.ai.mc.data

import de.fhe.ai.mc.model.Address
import de.fhe.ai.mc.model.Person
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepository : PanacheRepository<Person> {
    fun findAllByName(
        name: String,
        sort: Sort = Sort.by("id")
    ): Uni<List<Person>> = list("name LIKE ?1", sort, "%$name%")

    fun findAllByCity(city: String): Uni<List<Person>> =
        list("FROM Person p JOIN p.addresses a WHERE a.city LIKE :city",
            Parameters.with("city", "%$city%"))

}

@ApplicationScoped
class AddressRepository : PanacheRepository<Address> {
}