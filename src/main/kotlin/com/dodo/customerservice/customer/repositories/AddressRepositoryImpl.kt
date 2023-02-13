package com.dodo.customerservice.customer.repositories

import com.dodo.customerservice.customer.services.AddressModel
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class AddressRepositoryImpl: AddressRepository {
    override fun create(address: AddressModel, refCustomer: Customer): Address? {
        return try {
            transaction {
                Address.new {
                    streetName = address.streetName
                    city = address.city
                    postCode = address.postCode
                    state = address.state
                    country = address.country
                    customer = refCustomer
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun findByCustomer(customer: Customer): SizedIterable<Address>? {
        return try{
            var addresses: SizedIterable<Address>? = null
            transaction {
                addresses = Customer.findById(customer.id)?.load(Customer::addresses)?.addresses
            }
            addresses
        } catch (e: Exception) {
            null
        }
    }
}