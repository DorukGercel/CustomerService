package com.dodo.customerservice.customer.services

data class CustomerModel(
        val id: Int,
        val name: String,
        val email: String,
        val addresses: List<AddressModel>
) {
    constructor(name: String, email: String, addresses: List<AddressModel>): this(0, name, email, addresses)
}

data class AddressModel(
        val streetName: String,
        val city: String,
        val postCode: String,
        val state: String,
        val country: String,
)

data class CustomerQuery(
        val id: Int?,
        val name: String?,
        val email: String?,
)