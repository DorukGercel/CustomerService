package com.dodo.customerservice.customer.repositories

import com.dodo.customerservice.customer.definitions.testEmail
import com.dodo.customerservice.customer.definitions.testName
import com.dodo.customerservice.customer.services.CustomerModel
import com.dodo.customerservice.customer.services.CustomerQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test


class CustomerRepositoryImplTest {
    private val customerRepository: CustomerRepository = CustomerRepositoryImpl()
    companion object {
        @JvmStatic
        @BeforeAll
        fun connectDB() {
            Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver", user = "root", password = "")
            transaction {
                SchemaUtils.create(Customers, Addresses)
            }
        }
    }

    @Test
    fun createTest() {
        val customer = customerRepository.create(CustomerModel(
            name = testName,
            email = testEmail,
            addresses = listOf()
        ))
        assertNotNull(customer)
    }

    @Test
    fun firstTest() {
        var customerCreated: Customer? = null
        try {
            transaction {
                customerCreated = Customer.new {
                    name = testName
                    email = testEmail
                }
            }
        } catch (e: Exception) {
            assert(false){e}
        }
        // id fetch
        var customerFetched = customerRepository.first(CustomerQuery(customerCreated?.id?.value, null, null))
        assertNotNull(customerFetched)
        assertEquals(customerCreated?.id?.value, customerFetched?.id?.value)
        // name fetch
        customerFetched = customerRepository.first(CustomerQuery(null, customerCreated?.name, null))
        assertNotNull(customerFetched)
        assertEquals(customerCreated?.name, customerFetched?.name)
        // email fetch
        customerFetched = customerRepository.first(CustomerQuery(null, null, customerCreated?.email))
        assertNotNull(customerFetched)
        assertEquals(customerCreated?.email, customerFetched?.email)
    }

    @AfterEach
    fun cleanDB() {
        transaction {
            Addresses.deleteAll()
            Customers.deleteAll()
        }
    }
}