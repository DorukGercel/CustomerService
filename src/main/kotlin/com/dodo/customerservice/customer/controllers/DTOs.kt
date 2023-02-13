package com.dodo.customerservice.customer.controllers

import com.dodo.customerservice.customer.services.CustomerModel
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

open class ResponseDTO(
        private val statusCode: Int
)

data class CreateRequestDTO(
        @field:NotEmpty
        val name: String,
        @field:NotEmpty
        @field:Email
        val email: String,
        @field:NotEmpty
        val addresses: List<AddressDTO>
)

data class AddressDTO(
        val streetName: String,
        val city: String,
        val postCode: String,
        val state: String,
        val country: String,
)

data class CreateResponseDTO(
        val statusCode: Int
): ResponseDTO(statusCode)

data class FetchResponseDTO(
        val statusCode: Int,
        val customer: CustomerModel?
): ResponseDTO(statusCode) {
        constructor(statusCode: Int): this(statusCode, null)
}