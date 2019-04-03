package com.benjaminvega.crm.controller;

import com.benjaminvega.crm.model.User;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminControllerIT {

    @Autowired
    private AdminController cut;

    private User user;
    private static UserRepresentation userRepresentation;

    @Before
    public void setUp() {
        this.user = User.builder()
                .email("integrationTest@crm.com")
                .username("IntTest")
                .firstName("Integration")
                .password("password")
                .roles(Arrays.asList("user", "non_existing_role"))
                .build();
    }

    @Test
    public void a_createUser() {
        ResponseEntity<UserRepresentation> userRepresentationResponseEntity = cut.createUser(user);

        assertThat(userRepresentationResponseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        UserRepresentation userRepresentation = userRepresentationResponseEntity.getBody();
        assertThat(userRepresentation.getUsername()).isEqualTo(user.getUsername());
        assertThat(userRepresentation.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userRepresentation.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void b_listTheUserThatHasJustBeenCreated() {
        ResponseEntity<List<UserRepresentation>> userRepresentationResponseEntity = cut.listUsers();

        assertThat(userRepresentationResponseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        List<UserRepresentation> userRepresentationList = userRepresentationResponseEntity.getBody();
        assertNotNull(userRepresentationList);

        Optional<UserRepresentation> userRepresentationOptional = userRepresentationList.stream().filter(u -> u.getUsername().equals(user.getUsername().toLowerCase())).findFirst();
        assertThat(userRepresentationOptional).isPresent();
        userRepresentation = userRepresentationOptional.get();
    }

    @Test
    public void c_getTheUserThatHasBeenCreated() {
        ResponseEntity<UserRepresentation> userRepresentationResponseEntity = cut.getUser(userRepresentation.getId());

        assertThat(userRepresentationResponseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        UserRepresentation userResource = userRepresentationResponseEntity.getBody();
        assertThat(userResource.getUsername()).isEqualTo(user.getUsername().toLowerCase());
        assertThat(userResource.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userResource.getEmail()).isEqualTo(user.getEmail().toLowerCase());
    }

    @Test
    public void d_deleteUser() {
        ResponseEntity<Void> responseEntity = cut.deleteUser(userRepresentation.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ResponseEntity<UserRepresentation> userRepresentationResponseEntity = cut.getUser(userRepresentation.getId());

        assertThat(userRepresentationResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}
