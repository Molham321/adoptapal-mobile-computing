package de.fhe.ai.mc.model

import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

class ValueWithIpAddress(val value: String, val ipAddress: String) {
    override fun toString() = "$value from $ipAddress"
}

@Entity
class Contact() {

    @Id
    @GeneratedValue
    var id: Long = 0
    lateinit var name: String
    lateinit var firstname: String
    lateinit var mail: String

    constructor(name: String, firstname: String, mail: String) : this() {
        this.name = name;
        this.firstname = firstname
        this.mail = mail
    }

    override fun toString()= "Contact(id=$id, name='$name', firstname='$firstname', mail='$mail')"
}

@ApplicationScoped
class ContactRepository : PanacheRepository<Contact>

// -------------------
// Models & Repos for Messaging Example
// -------------------

@Embeddable
class Location() {
    lateinit var latitude:String
    lateinit var longitude: String

    constructor(latitude:String, longitude: String): this() {
        this.latitude = latitude
        this.longitude = longitude
    }

    override fun toString(): String {
        return "Location(latitude='$latitude', longitude='$longitude')"
    }
}

@Embeddable
class Address() {
    var city: String? = null
    var state: String? = null
    var postcode: String? = null
    var country: String? = null

    constructor(city: String, state: String, postcode: String, country: String) : this() {
        this.city = city
        this.state = state
        this.postcode = postcode
        this.country = country
    }

    override fun toString(): String {
        return "Address(city='$city', state='$state', postcode='$postcode', country='$country')"
    }
}

@Entity
class GeocodingResult() {
    @Id
    @GeneratedValue
    var id: Long = 0
    @Embedded
    var location: Location? = null
    @Embedded
    var address: Address? = null

    override fun toString(): String {
        return "GeocodingResult(id=$id, location=$location, address=$address)"
    }
}

@ApplicationScoped
class GeocodingResultRepository : PanacheRepository<GeocodingResult>