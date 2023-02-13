package com.dodo.customerservice.customer.repositories

import com.dodo.customerservice.customer.services.AddressModel
import org.jetbrains.exposed.sql.SizedIterable

interface AddressRepository {
    fun create(address: AddressModel, refCustomer: Customer): Address?
    fun findByCustomer(customer: Customer): SizedIterable<Address>?
}