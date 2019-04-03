package com.benjaminvega.crm.controller;

import com.benjaminvega.crm.model.User;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    String realm;

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/users")
    public ResponseEntity<UserRepresentation> createUser(@RequestBody @Valid User userRequest) {

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userRequest.getPassword());
        credential.setTemporary(false);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setCredentials(Arrays.asList(credential));
        user.setEnabled(true);
        user.setRealmRoles(userRequest.getRoles());

        Response result = keycloak.realm(realm).users().create(user);
        if (result.getStatus() != 201) {
            logger.trace("The user: " + userRequest.getEmail() + " could not be created");
        }

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserRepresentation>> listUsers() {

        List<UserRepresentation> users = keycloak.realm(realm).users().list();
        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserRepresentation> getUser(@PathVariable("userId") String userId) {

        List<UserRepresentation> users = keycloak.realm(realm).users().list();
        Optional<UserRepresentation> user = users.stream().filter((UserRepresentation u) -> u.getId().compareTo(userId) == 0).findFirst();
        return user.map(userRepresentation -> new ResponseEntity<>(userRepresentation, HttpStatus.ACCEPTED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {

        keycloak.realm(realm).users().get(userId).remove();
        logger.trace("The user with id: " + userId + " has just been deleted");

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}