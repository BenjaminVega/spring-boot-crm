package com.benjaminvega.crm.converters;


import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.vo.CustomerRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerRequestToCustomerConverter implements Converter<CustomerRequest, Customer> {

    @Override
    public Customer convert(CustomerRequest customerRequest) {
        return Customer.builder()
                .name(customerRequest.getName())
                .surname(customerRequest.getSurname())
                .pictureId(customerRequest.getPictureId())
                .build();
    }
}
