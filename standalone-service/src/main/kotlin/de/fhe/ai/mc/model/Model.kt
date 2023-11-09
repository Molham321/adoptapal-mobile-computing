package de.fhe.ai.mc.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


@Entity
open class Person() {
    @Id @GeneratedValue
    open var id: Long = 0
    open lateinit var name: String

    @OneToMany(mappedBy = "person", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
    open var addresses: MutableList<Address> = mutableListOf()

    constructor(name: String) : this() {
        this.name = name
    }

    override fun toString(): String {
        return "Person(id=$id, name='$name', addresses=$addresses)"
    }
}

@Entity
open class Address() {
    @Id @GeneratedValue
    open var id: Long = 0
    @ManyToOne @JsonIgnore
    open lateinit var person: Person
    open lateinit var street: String
    open lateinit var city: String
    open lateinit var zipCode: String

    constructor(person: Person, street: String, city: String, zipCode: String) : this() {
        this.person = person
        this.street = street
        this.city = city
        this.zipCode = zipCode
    }

    override fun toString(): String {
        return "Address(id=$id, street='$street', city='$city', zipCode='$zipCode')"
    }
}