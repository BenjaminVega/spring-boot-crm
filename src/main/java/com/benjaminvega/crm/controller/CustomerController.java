package com.benjaminvega.crm.controller;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.CustomerView;
import com.benjaminvega.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ConversionService conversionService;

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") long customerId) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        if (customer != null ){
            status = HttpStatus.ACCEPTED;
        }

        return ResponseEntity.status(status).body(customer);
    }

    @PostMapping
    public ResponseEntity<Customer> postNewCustomer(@RequestBody @Valid CustomerView customer ){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Customer customerResponse = customerService.createNewCustomer(
                conversionService.convert(customer, Customer.class)
        );

        if (customer != null ){
            status = HttpStatus.ACCEPTED;
        }

        return ResponseEntity.status(status).body(customerResponse);
    }

}
