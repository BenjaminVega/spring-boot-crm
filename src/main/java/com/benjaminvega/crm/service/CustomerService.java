package com.benjaminvega.crm.service;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer getCustomerByCustomerId(long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.orElse(null);
    }

    public Customer createNewCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void deleteCustomer(long customerId) {
        customerRepository.deleteById(customerId);
    }

    public Customer updateCustomer(Customer customer, long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            optionalCustomer.get().setName(customer.getName());
            optionalCustomer.get().setSurname(customer.getSurname());
            optionalCustomer.get().setPictureId(customer.getPictureId());
            return customerRepository.save(optionalCustomer.get());
        }
        return  null;
    }
}
