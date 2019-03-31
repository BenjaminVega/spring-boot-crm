package com.benjaminvega.crm.journey;

import com.benjaminvega.crm.model.Customer;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CustomerE2E {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080/customers";

    }

    @Test
    public void createNewCustomer() {
        Customer newCustomer = Customer.builder()
                .name("Tom")
                .surname("Jones")
                .editorId(1L)
                .build();

        Customer actualCustomer =
            RestAssured
                .given()
                    .contentType("application/json")
                    .body(newCustomer)
                .post()
                .then()
                    .extract()
                    .as(Customer.class);

        assertThat(actualCustomer.getId()).isGreaterThan(0L);
        assertThat(actualCustomer.getName()).isEqualTo(newCustomer.getName());
        assertThat(actualCustomer.getSurname()).isEqualTo(newCustomer.getSurname());
        assertThat(actualCustomer.getEditorId()).isEqualTo(newCustomer.getEditorId());

    }
}
