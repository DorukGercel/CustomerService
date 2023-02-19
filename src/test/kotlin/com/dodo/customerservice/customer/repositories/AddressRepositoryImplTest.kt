package com.dodo.customerservice.customer.repositories

import com.dodo.customerservice.customer.definitions.*
import com.dodo.customerservice.customer.services.AddressModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class AddressRepositoryImplTest {
    private val addressRepository: AddressRepository = AddressRepositoryImpl()
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
        // Test setup
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
            return
        }

        val refCustomer: Customer
        try {
            refCustomer = customerCreated as Customer
        } catch (e: Exception) {
            assert(false){e}
            return
        }

        // Test
        val address = addressRepository.create(
            AddressModel(
                streetName = testStreet,
                city = testCity,
                postCode = testPostCode,
                state = testState,
                country = testCountry
            ),refCustomer)
        Assertions.assertNotNull(address)
    }

    @Test
    fun findByCustomerTest() {
        // Test setup
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
            return
        }

        val refCustomer: Customer
        try {
            refCustomer = customerCreated as Customer
        } catch (e: Exception) {
            assert(false){e}
            return
        }

        var addressCreated: Address? = null
        try {
            transaction {
                addressCreated = Address.new {
                    streetName = testStreet
                    city = testCity
                    postCode = testPostCode
                    state = testState
                    country = testCountry
                    customer = refCustomer
                }
            }
        } catch (e: Exception) {
            assert(false){e}
            return
        }

        // Test
        val addressesFetched = addressRepository.findByCustomer(refCustomer)
        if (addressesFetched != null) {
            Assertions.assertEquals(1, addressesFetched.count())
        } else {
            assert(false)
            return
        }
        val address = addressesFetched.elementAt(0)
        Assertions.assertEquals(addressCreated?.streetName, address.streetName)
        Assertions.assertEquals(addressCreated?.city, address.city)
        Assertions.assertEquals(addressCreated?.postCode, address.postCode)
        Assertions.assertEquals(addressCreated?.state, address.state)
        Assertions.assertEquals(addressCreated?.country, address.country)
    }

    @AfterEach
    fun cleanDB() {
        transaction {
            Addresses.deleteAll()
            Customers.deleteAll()
        }
    }
}