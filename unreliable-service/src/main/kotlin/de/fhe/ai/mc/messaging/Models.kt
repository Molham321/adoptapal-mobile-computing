package de.fhe.ai.mc.messaging

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

class GeocodingResult() {
    var id: Long = 0
    var location: Location? = null
    var address: Address? = null

    override fun toString(): String {
        return "GeocodingResult(id=$id, location=$location, address=$address)"
    }
}