package com.benjaminvega.crm.service;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService cut;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void getCustomerByIdSuccess() {
        long customerId = 9378;
        Customer expectedCustomer = Customer.builder()
                .id(customerId)
                .name("Michael")
                .surname("Jackson")
                .editorId(1L)
                .pictureId(1L)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer));

        Customer actualCustomer = cut.getCustomerByCustomerId(customerId);

        verify(customerRepository).findById(customerId);
        assertThat(actualCustomer).isEqualTo(expectedCustomer);

    }

    @Test
    public void getCustomerByIdFailed() {
        long customerId = 9378;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Customer actualCustomer = cut.getCustomerByCustomerId(customerId);

        assertThat(actualCustomer).isNull();
        verify(customerRepository).findById(customerId);
    }
}
