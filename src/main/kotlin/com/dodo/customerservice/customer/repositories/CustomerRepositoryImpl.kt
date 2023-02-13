package com.dodo.customerservice.customer.repositories

import com.dodo.customerservice.customer.services.CustomerModel
import com.dodo.customerservice.customer.services.CustomerQuery
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CustomerRepositoryImpl: CustomerRepository {
    override fun create(customer: CustomerModel): Customer? {
        return try {
            transaction {
                Customer.new {
                    name = customer.name
                    email = customer.email
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun first(customerQuery: CustomerQuery): Customer? {
        return try {
            transaction {
                Customer.find {
                    (if(customerQuery.id != null) Customers.id eq customerQuery.id
                    else if(customerQuery.name != null) Customers.name eq customerQuery.name
                    else if(customerQuery.email != null) Customers.email eq customerQuery.email
                    else Op.FALSE)
                }.limit(1).single()
            }
        } catch (e: Exception) {
            null
        }
    }
}