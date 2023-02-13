package com.dodo.customerservice.customer.controllers

import com.dodo.customerservice.customer.definitions.*
import com.dodo.customerservice.customer.services.AddressModel
import com.dodo.customerservice.customer.services.CustomerModel
import com.dodo.customerservice.customer.services.CustomerQuery
import com.dodo.customerservice.customer.services.CustomerService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@WebMvcTest
class CustomerControllerTest(@Autowired val mockMvc: MockMvc) {
    
    @MockkBean
    lateinit var customerService: CustomerService

    @Test
    fun createTest_success() {
        val requestJson: String = ObjectMapper().
            writer().
            withDefaultPrettyPrinter().
            writeValueAsString(CreateRequestDTO(name = testName, email = testEmail,
                addresses = listOf(AddressDTO(streetName = testStreet, city = testCity, postCode = testPostCode, state = testState, country = testCountry))))

        every { customerService.create(any()) } returns OpCode.SUCCESS

        mockMvc.perform(post("/customers").content(requestJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
    }

    @Test
    fun createTest_fail_emptyCustomerName() {
        val requestJson: String = ObjectMapper().
        writer().
        withDefaultPrettyPrinter().
        writeValueAsString(CreateRequestDTO(name = "", email = testEmail,
            addresses = listOf(AddressDTO(streetName = testStreet, city = testCity, postCode = testPostCode, state = testState, country = testCountry))))

        every { customerService.create(any()) } returns OpCode.SUCCESS

        mockMvc.perform(post("/customers").content(requestJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun createTest_fail_wrongCustomerEmailFormat() {
        val requestJson: String = ObjectMapper().
        writer().
        withDefaultPrettyPrinter().
        writeValueAsString(CreateRequestDTO(name = testName, email = "testEmail",
            addresses = listOf(AddressDTO(streetName = testStreet, city = testCity, postCode = testPostCode, state = testState, country = testCountry))))

        every { customerService.create(any()) } returns OpCode.SUCCESS

        mockMvc.perform(post("/customers").content(requestJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun createTest_fail_emptyAddressField() {
        val requestJson: String = ObjectMapper().
        writer().
        withDefaultPrettyPrinter().
        writeValueAsString(CreateRequestDTO(name = testName, email = testEmail,
            addresses = emptyList()))

        every { customerService.create(any()) } returns OpCode.SUCCESS

        mockMvc.perform(post("/customers").content(requestJson).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun fetchTest_id_success() {
        val customer = CustomerModel(name = testName, email = testEmail, addresses = listOf(AddressModel(streetName = testStreet, city = testCity, postCode = testPostCode, state = testState, country = testCountry)))

        every { customerService.fetch(any()) } returns Pair(OpCode.INTERNAL_ERROR, null)
        every { customerService.fetch(CustomerQuery(testCustomerID, null, null)) } returns Pair(OpCode.SUCCESS, customer)

        mockMvc.perform(get("/customers?id=$testCustomerID"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.customer.id").value(customer.id))
    }

    @Test
    fun fetchTest_name_success() {
        val customer = CustomerModel(name = testName, email = testEmail, addresses = listOf(AddressModel(streetName = testStreet, city = testCity, postCode = testPostCode, state = testState, country = testCountry)))

        every { customerService.fetch(any()) } returns Pair(OpCode.INTERNAL_ERROR, null)
        every { customerService.fetch(CustomerQuery(null, testName, null)) } returns Pair(OpCode.SUCCESS, customer)

        mockMvc.perform(get("/customers?name=$testName"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.customer.name").value(customer.name))
    }

    @Test
    fun fetchTest_email_success() {
        val customer = CustomerModel(name = testName, email = testEmail, addresses = listOf(AddressModel(streetName = testStreet, city = testCity, postCode = testPostCode, state = testState, country = testCountry)))

        every { customerService.fetch(any()) } returns Pair(OpCode.INTERNAL_ERROR, null)
        every { customerService.fetch(CustomerQuery(null, null, testEmail)) } returns Pair(OpCode.SUCCESS, customer)

        mockMvc.perform(get("/customers?email=$testEmail"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.customer.email").value(customer.email))
    }
}