package com.dodo.customerservice.customer.services

import com.dodo.customerservice.customer.definitions.OpCode

interface CustomerService {
    fun create(customer: CustomerModel): OpCode
    fun fetch(query: CustomerQuery): Pair<OpCode, CustomerModel?>

}