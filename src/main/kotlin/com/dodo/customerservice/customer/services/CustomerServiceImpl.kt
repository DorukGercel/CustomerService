package com.dodo.customerservice.customer.services

import com.dodo.customerservice.customer.definitions.OpCode
import com.dodo.customerservice.customer.repositories.AddressRepository
import com.dodo.customerservice.customer.repositories.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(private val customerRepository: CustomerRepository, private val addressRepository: AddressRepository):
    CustomerService {
    override fun create(customer: CustomerModel): OpCode {
        val newCustomer = customerRepository.create(customer) ?: return OpCode.INTERNAL_ERROR
        for (addr in customer.addresses) {
            if(addressRepository.create(addr, newCustomer) == null) {
                return OpCode.INTERNAL_ERROR
            }
        }
        return OpCode.SUCCESS
    }

    override fun fetch(query: CustomerQuery): Pair<OpCode, CustomerModel?> {
        if(query.id == null && query.name == null && query.email == null) {
            return Pair(OpCode.INTERNAL_ERROR, null)
        }
        val customer = customerRepository.first(query) ?: return Pair(OpCode.INTERNAL_ERROR, null)
        val customerAddresses = addressRepository.findByCustomer(customer) ?: return Pair(OpCode.INTERNAL_ERROR, null)
        val addresses = arrayListOf<AddressModel>()
        for(addr in customerAddresses) {
            addresses.add(
                AddressModel(
                    streetName = addr.streetName,
                    city = addr.city,
                    postCode = addr.postCode,
                    state = addr.state,
                    country = addr.country
            )
            )
        }
        return Pair(
            OpCode.SUCCESS, CustomerModel(
                id = customer.id.value,
                name = customer.name,
                email = customer.email,
                addresses = addresses
        )
        )
    }

}