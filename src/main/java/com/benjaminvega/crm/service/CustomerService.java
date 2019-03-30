package com.benjaminvega.crm.service;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer getCustomerByCustomerId(long customerId){
        Optional<Customer> customer =  customerRepository.findById(customerId);
        return customer.orElse(null);
    }
}
