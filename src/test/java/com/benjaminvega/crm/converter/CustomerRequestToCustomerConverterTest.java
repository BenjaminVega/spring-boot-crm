package com.benjaminvega.crm.converter;

import com.benjaminvega.crm.converters.CustomerRequestToCustomerConverter;
import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.vo.CustomerRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CustomerRequestToCustomerConverterTest {

    @InjectMocks
    private CustomerRequestToCustomerConverter cut;

    private String customerName;
    private String customerSurname;
    private long customerPictureId;

    @Before
    public void setUp() {
        customerName = "Bruce";
        customerSurname = "Wane";
        customerPictureId = 9823L;
    }

    @Test
    public void successfulConversion() {
        Customer expectedCustomer = Customer.builder()
                .name(customerName)
                .surname(customerSurname)
                .pictureId(customerPictureId)
                .build();
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name(customerName)
                .surname(customerSurname)
                .pictureId(customerPictureId)
                .build();

        Customer actualCustomer = cut.convert(customerRequest);


        assertThat(actualCustomer.getSurname()).isEqualTo(expectedCustomer.getSurname());
        assertThat(actualCustomer.getName()).isEqualTo(expectedCustomer.getName());
        assertThat(actualCustomer.getPictureId()).isEqualTo(expectedCustomer.getPictureId());
    }

}
