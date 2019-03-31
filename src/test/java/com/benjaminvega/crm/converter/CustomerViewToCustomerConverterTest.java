package com.benjaminvega.crm.converter;

import com.benjaminvega.crm.converters.CustomerViewToCustomerConverter;
import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.CustomerView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CustomerViewToCustomerConverterTest {

    @InjectMocks
    private CustomerViewToCustomerConverter cut;

    private String customerName;
    private String customerSurname;
    private long customerEditorId;
    private long customerPictureId;

    @Before
    public void setUp() {
        customerName = "Bruce";
        customerSurname = "Wane";
        customerEditorId = 124L;
        customerPictureId = 9823L;
    }

    @Test
    public void successfulConversion() {
        Customer expectedCustomer = Customer.builder()
                .name(customerName)
                .surname(customerSurname)
                .editorId(customerEditorId)
                .pictureId(customerPictureId)
                .build();
        CustomerView customerView = CustomerView.builder()
                .name(customerName)
                .surname(customerSurname)
                .editorId(customerEditorId)
                .pictureId(customerPictureId)
                .build();

        Customer actualCustomer = cut.convert(customerView);

        assertThat(actualCustomer).isEqualTo(expectedCustomer);
    }

}
