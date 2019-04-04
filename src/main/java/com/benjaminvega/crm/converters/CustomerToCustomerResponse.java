package com.benjaminvega.crm.converters;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.vo.CustomerResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerToCustomerResponse implements Converter<Customer, CustomerResponse> {

    private static final String BASE_URL = "http://localhost:8080/pictures/";
    @Override
    public CustomerResponse convert(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .editorId(customer.getEditorId())
                .pictureUrl(BASE_URL + customer.getPictureId())
                .build();
    }
}
