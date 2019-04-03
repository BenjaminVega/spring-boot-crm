package com.benjaminvega.crm.service;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.File;
import com.benjaminvega.crm.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    public void getAllCustomers() {
        Customer expectedCustomer = Customer.builder()
                .id(234L)
                .name("Michael")
                .surname("Jackson")
                .editorId(1L)
                .pictureId(1L)
                .build();
        when(customerRepository.findAll()).thenReturn(Arrays.asList(expectedCustomer));

        List<Customer> customers = cut.getAllCustomers();
        assertNotNull(customers);
    }

    @Test
    public void deleteCustomer() {
        cut.deleteCustomer(789L);
    }

    @Test
    public void updateCustomer() {
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Customer customer = Customer.builder()
                .id(234L)
                .name("Michael")
                .surname("Jackson")
                .editorId(1L)
                .pictureId(1L)
                .build();

        Customer expectedCustomer = Customer.builder()
                .id(234L)
                .name("Jake")
                .surname("Jackson")
                .editorId(1L)
                .pictureId(1L)
                .build();
        when(customerRepository.findById(234L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(expectedCustomer);

        cut.updateCustomer(expectedCustomer, 234L);
        verify(customerRepository).save(argumentCaptor.capture());

        assertThat(expectedCustomer.getName()).isEqualTo(argumentCaptor.getValue().getName());
    }

    @Test
    public void updateACustomerThatDoesNotExists() {
        Customer customer = cut.updateCustomer(null, 234L);
        assertNull(customer);
    }
}
