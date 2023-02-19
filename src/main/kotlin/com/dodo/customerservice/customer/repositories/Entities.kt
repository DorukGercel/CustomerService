package com.dodo.customerservice.customer.repositories

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Customers : IntIdTable() {
    val name = text("name").uniqueIndex()
    val email = text("email").uniqueIndex()
}

object Addresses: IntIdTable() {
    val customer = reference("customer", Customers)
    val streetName = text("street_name")
    val city = text("city")
    val postCode = text("post_code")
    val state = text("state")
    val country = text("country")
}

class Customer(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Customer>(Customers)

    var name by Customers.name
    var email by Customers.email

    val addresses by Address referrersOn Addresses.customer
}

class Address(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Address>(Addresses)

    var streetName by Addresses.streetName
    var city by Addresses.city
    var postCode by Addresses.postCode
    var state by Addresses.state
    var country by Addresses.country

    var customer by Customer referencedOn Addresses.customer
}