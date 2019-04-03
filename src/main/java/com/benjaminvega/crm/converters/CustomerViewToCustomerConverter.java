package com.benjaminvega.crm.converters;


import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.CustomerView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerViewToCustomerConverter implements Converter<CustomerView, Customer> {

    @Override
    public Customer convert(CustomerView customerView) {
        return Customer.builder()
                .name(customerView.getName())
                .surname(customerView.getSurname())
                .editorId(111L)
                .pictureId(customerView.getPictureId())
                .build();
    }
}
