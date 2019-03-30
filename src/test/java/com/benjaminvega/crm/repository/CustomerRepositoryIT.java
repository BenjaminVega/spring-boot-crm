package com.benjaminvega.crm.repository;

import com.benjaminvega.crm.model.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryIT {

    @Autowired
    private CustomerRepository cut;

    @Test
    public void addCustomerToDBAndReadItBack() {
        long customerId = 1L;


        Customer expectedCustomer = Customer.builder()
                .name("Michael")
                .surname("Jackson")
                .pictureId(123L)
                .editorId(321L)
                .build();

        cut.save(expectedCustomer);

        Optional<Customer> actualCustomer = cut.findById(customerId);

        assertThat(actualCustomer.isPresent()).isTrue();
        assertThat(actualCustomer.get()).isEqualTo(expectedCustomer);

    }

}
