package com.dodo.customerservice.customer.controllers

import com.dodo.customerservice.customer.*
import com.dodo.customerservice.customer.definitions.OpCode
import com.dodo.customerservice.customer.services.AddressModel
import com.dodo.customerservice.customer.services.CustomerModel
import com.dodo.customerservice.customer.services.CustomerQuery
import com.dodo.customerservice.customer.services.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers")
class CustomerController(private val customerService: CustomerService){
    @PostMapping("")
    fun create(@Valid @RequestBody req: CreateRequestDTO): CreateResponseDTO {
        val addresses = ArrayList<AddressModel>()
        for(addr in req.addresses) {
            addresses.add(AddressModel(streetName = addr.streetName, city = addr.city, postCode = addr.postCode, state = addr.state, country = addr.country))
        }
        val opCode = customerService.create(CustomerModel(name = req.name, email = req.email, addresses = addresses))
        return if (opCode != OpCode.SUCCESS) {
            CreateResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value())
        } else {
            CreateResponseDTO(HttpStatus.OK.value())
        }
    }

    @GetMapping("")
    fun fetch(@RequestParam id: Int?, @RequestParam name: String?, @RequestParam email: String?): FetchResponseDTO {
        if(id == null && name == null && email == null) {
            return FetchResponseDTO(HttpStatus.BAD_REQUEST.value())
        }
        val (opCode, customers) = customerService.fetch(CustomerQuery(id = id, name = name, email = email))
        return if (opCode != OpCode.SUCCESS || customers == null) {
            FetchResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value())
        } else {
            FetchResponseDTO(HttpStatus.OK.value(), customers)
        }
    }
}