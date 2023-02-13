package com.dodo.customerservice.customer.repositories

import com.dodo.customerservice.customer.services.CustomerModel
import com.dodo.customerservice.customer.services.CustomerQuery

interface CustomerRepository {
    fun create(customer: CustomerModel): Customer?
    fun first(customerQuery: CustomerQuery): Customer?
}