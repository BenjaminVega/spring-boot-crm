package com.benjaminvega.crm.controller;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.CustomerView;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.DEFAULT)
public class CustomerControllerIT {

    @Autowired
    public CustomerController cut;

    @Before
    public void setup() {

    }

    @Test
    public void firstCreateNewCustomerSuccess() {
        CustomerView customerView = CustomerView.builder()
                .name("Michael")
                .surname("Jackson")
                .editorId(123L)
                .pictureId(321L)
                .build();


        ResponseEntity<Customer> customerResponse = cut.postNewCustomer(customerView);
        assertThat(customerResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        Customer actualCustomer = customerResponse.getBody();

        assertThat(actualCustomer.getId()).isGreaterThan(0L);
        assertThat(actualCustomer.getName()).isEqualTo(customerView.getName());
        assertThat(actualCustomer.getSurname()).isEqualTo(customerView.getSurname());
        assertThat(actualCustomer.getEditorId()).isEqualTo(customerView.getEditorId());

    }

    @Test
    public void secondGetFirstCustomerAddedInDb() {
        long customerId = 1L;

        ResponseEntity<Customer> customerResponse = cut.getCustomerById(customerId);
        assertThat(customerResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

    }

    @Test(expected = ConstraintViolationException.class)
    public void postNewCustomerWithInvalidStructure() {
        CustomerView customerView = CustomerView.builder()
                .name("Tom")
                .build();

        cut.postNewCustomer(customerView);
    }

    @Test
    public void getCustomerThatDoesNotExist() {
        long customerId = 789L;

        ResponseEntity<Customer> customerResponse = cut.getCustomerById(customerId);
        assertThat(customerResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
