package com.benjaminvega.crm.controller;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("customers/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") long customerId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerService.getCustomerByCustomerId(customerId));
    }

}
