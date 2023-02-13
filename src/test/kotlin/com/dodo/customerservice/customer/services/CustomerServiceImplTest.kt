package com.dodo.customerservice.customer.services

import com.dodo.customerservice.customer.definitions.*
import com.dodo.customerservice.customer.repositories.*
import io.mockk.every
import io.mockk.mockk
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedCollection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class CustomerServiceImplTest {
    private val customerRepository: CustomerRepository = mockk()
    private val addressRepository: AddressRepository = mockk()
    private val customerService: CustomerService = CustomerServiceImpl(
        customerRepository = customerRepository,
        addressRepository = addressRepository
    )

    @Test
    fun createTest_success() {
        val customerModel = CustomerModel(
            id = testCustomerID,
            name = testName,
            email = testEmail,
            addresses = listOf(AddressModel(streetName = testStreet, city = testCity, postCode = testPostCode, state = testState, country = testCountry))
        )
        val customer = Customer(EntityID(testCustomerID, Customers))
        val address = Address(EntityID(testAddressID, Addresses))
        //given
        every { customerRepository.create(any()) } returns customer
        every { addressRepository.create(any(), any()) } returns address

        //when
        val result = customerService.create(customer = customerModel)

        //then
        assertEquals(OpCode.SUCCESS, result)
    }

    @Test
    fun createTest_fail_customerCreate() {
        val customerModel = CustomerModel(
            id = testCustomerID,
            name = testName,
            email = testEmail,
            addresses = listOf(AddressModel(streetName = testStreet, city = testCity, postCode = testPostCode, state = testState, country = testCountry))
        )
        val address = Address(EntityID(testAddressID, Addresses))
        //given
        every { customerRepository.create(any()) } returns null
        every { addressRepository.create(any(), any()) } returns address

        //when
        val result = customerService.create(customer = customerModel)

        //then
        assertEquals(OpCode.INTERNAL_ERROR, result)
    }

    @Test
    fun createTest_fail_addressCreate() {
        val customerModel = CustomerModel(
            id = testCustomerID,
            name = testName,
            email = testEmail,
            addresses = listOf(AddressModel(streetName = testStreet, city = testCity, postCode = testPostCode, state = testState, country = testCountry))
        )
        val customer = Customer(EntityID(testCustomerID, Customers))
        //given
        every { customerRepository.create(any()) } returns customer
        every { addressRepository.create(any(), any()) } returns null

        //when
        val result = customerService.create(customer = customerModel)

        //then
        assertEquals(OpCode.INTERNAL_ERROR, result)
    }

    @Test
    fun fetchTest_id_success_resultFound() {
        val customerQuery = CustomerQuery(
            id = testCustomerID, email = null, name = null
        )
        val mockedCustomer = mockk<Customer> {
            mockk {
                every { id } returns EntityID(testCustomerID, Customers)
                every { name } returns testName
                every { email } returns testEmail
            }
        }
        val mockedAddress = mockk<Address> {
            mockk {
                every { id } returns EntityID(testAddressID, Addresses)
                every { streetName } returns testStreet
                every { city } returns testCity
                every { postCode } returns testPostCode
                every { state } returns testState
                every { country } returns testCountry
                every { customer } returns mockedCustomer
            }
        }
        val mockedAddresses = SizedCollection(mockedAddress)
        //given
        every { customerRepository.first(any()) } returns mockedCustomer
        every { addressRepository.findByCustomer(any())} returns mockedAddresses

        //when
        val (resultCode, resultCustomer) = customerService.fetch(customerQuery)

        //then
        assertEquals(OpCode.SUCCESS, resultCode)
        assertEquals(mockedCustomer.id.value, resultCustomer?.id)
    }

    @Test
    fun fetchTest_email_success_resultFound() {
        val customerQuery = CustomerQuery(
            id = null, email = testEmail, name = null
        )
        val mockedCustomer = mockk<Customer> {
            mockk {
                every { id } returns EntityID(testCustomerID, Customers)
                every { name } returns testName
                every { email } returns testEmail
            }
        }
        val mockedAddress = mockk<Address> {
            mockk {
                every { id } returns EntityID(testAddressID, Addresses)
                every { streetName } returns testStreet
                every { city } returns testCity
                every { postCode } returns testPostCode
                every { state } returns testState
                every { country } returns testCountry
                every { customer } returns mockedCustomer
            }
        }
        val mockedAddresses = SizedCollection(mockedAddress)
        //given
        every { customerRepository.first(any()) } returns mockedCustomer
        every { addressRepository.findByCustomer(any())} returns mockedAddresses

        //when
        val (resultCode, resultCustomer) = customerService.fetch(customerQuery)

        //then
        assertEquals(OpCode.SUCCESS, resultCode)
        assertEquals(mockedCustomer.email, resultCustomer?.email)
    }

    @Test
    fun fetchTest_name_success_resultFound() {
        val customerQuery = CustomerQuery(
            id = null, email = null, name = testName
        )
        val mockedCustomer = mockk<Customer> {
            mockk {
                every { id } returns EntityID(testCustomerID, Customers)
                every { name } returns testName
                every { email } returns testEmail
            }
        }
        val mockedAddress = mockk<Address> {
            mockk {
                every { id } returns EntityID(testAddressID, Addresses)
                every { streetName } returns testStreet
                every { city } returns testCity
                every { postCode } returns testPostCode
                every { state } returns testState
                every { country } returns testCountry
                every { customer } returns mockedCustomer
            }
        }
        val mockedAddresses = SizedCollection(mockedAddress)
        //given
        every { customerRepository.first(any()) } returns mockedCustomer
        every { addressRepository.findByCustomer(any())} returns mockedAddresses

        //when
        val (resultCode, resultCustomer) = customerService.fetch(customerQuery)

        //then
        assertEquals(OpCode.SUCCESS, resultCode)
        assertEquals(mockedCustomer.name, resultCustomer?.name)
    }

    @Test
    fun fetchTest_fail_resultNotFound() {
        val customerQuery = CustomerQuery(
            id = testCustomerID, email = null, name = null
        )
        //given
        every { customerRepository.first(any()) } returns null
        every { addressRepository.findByCustomer(any())} returns null

        //when
        val (resultCode, resultCustomer) = customerService.fetch(customerQuery)

        //then
        assertEquals(OpCode.INTERNAL_ERROR, resultCode)
        assertEquals(null, resultCustomer)
    }

    @Test
    fun fetchTest_fail_nullQuery() {
        val customerQuery = CustomerQuery(
            id = null, email = null, name = null
        )
        //when
        val (resultCode, resultCustomer) = customerService.fetch(customerQuery)

        //then
        assertEquals(OpCode.INTERNAL_ERROR, resultCode)
        assertEquals(null, resultCustomer)
    }
}
