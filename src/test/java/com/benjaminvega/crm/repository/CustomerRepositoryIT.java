package com.benjaminvega.crm.repository;

import com.benjaminvega.crm.model.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryIT {

    @Autowired
    private CustomerRepository cut;

    @Test
    public void addCustomerToDBAndReadItBack() {

        Customer expectedCustomer = Customer.builder()
                .name("Michael")
                .surname("Jackson")
                .editorId(UUID.randomUUID())
                .pictureId(321L)
                .build();

        Customer customer = cut.save(expectedCustomer);

        Optional<Customer> actualCustomer = cut.findById(customer.getId());

        if (actualCustomer.isPresent()) {
            assertThat(actualCustomer.get().getName()).isEqualTo(expectedCustomer.getName());
            assertThat(actualCustomer.get().getSurname()).isEqualTo(expectedCustomer.getSurname());
            assertThat(actualCustomer.get().getEditorId()).isEqualTo(expectedCustomer.getEditorId());
            assertThat(actualCustomer.get().getPictureId()).isEqualTo(expectedCustomer.getPictureId());

        } else {
            fail("No customer retrieved from database");
        }
    }
}
