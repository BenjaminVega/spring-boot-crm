package com.benjaminvega.crm.controller;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.File;
import com.benjaminvega.crm.model.vo.CustomerRequest;
import com.benjaminvega.crm.model.vo.CustomerResponse;
import com.benjaminvega.crm.model.vo.FileResponse;
import com.benjaminvega.crm.service.CustomerService;
import com.benjaminvega.crm.service.FileService;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ConversionService conversionService;

    private Logger logger = LoggerFactory.getLogger(CustomerController.class);


    @PostMapping("/customers")
    public ResponseEntity<CustomerResponse> postNewCustomer(@RequestBody @Valid CustomerRequest customerRequest, @NotNull Authentication auth) throws IOException {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        UUID editorId = UUID.fromString(((SimpleKeycloakAccount) auth.getDetails()).getKeycloakSecurityContext().getToken().getSubject());

        Customer customerToPersist = conversionService.convert(customerRequest, Customer.class);
        customerToPersist.setEditorId(editorId);

        Customer customer = customerService.createNewCustomer(customerToPersist);

        if (customer != null) {
            if (customer.getPictureId() != 0L) {
                fileService.updatePicture(customer.getPictureId(), customer.getId());
            }
            status = HttpStatus.ACCEPTED;
        }

        return ResponseEntity.status(status).body(conversionService.convert(customer, CustomerResponse.class));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> listCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerResponse> customersResponse = new ArrayList<>();
        customers.forEach(u -> customersResponse.add(conversionService.convert(u, CustomerResponse.class)));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customersResponse);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable("customerId") long customerId) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        if (customer != null) {
            status = HttpStatus.ACCEPTED;
        }

        return ResponseEntity.status(status).body(conversionService.convert(customer, CustomerResponse.class));
    }

    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @PatchMapping("/customers/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@RequestBody @Valid CustomerRequest customerRequest, @PathVariable("customerId") long customerId, @NotNull Authentication auth) {
        UUID editorId = UUID.fromString(((SimpleKeycloakAccount) auth.getDetails()).getKeycloakSecurityContext().getToken().getSubject());

        Customer customer = conversionService.convert(customerRequest, Customer.class);
        customer.setEditorId(editorId);

        Customer customerUpdated = customerService.updateCustomer(customer, customerId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(conversionService.convert(customerUpdated, CustomerResponse.class));
    }

    @PostMapping("/pictures")
    public ResponseEntity<FileResponse> uploadNewPicture(@RequestParam("file") MultipartFile picture) {
        try {
            File file = fileService.addPicture(picture);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new FileResponse(file.getId()));
        } catch (Exception e) {
            logger.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    @GetMapping("/pictures/{pictureId}")
    public ResponseEntity<Resource> getPictureById(@PathVariable("pictureId") long pictureId) {
        Resource picture = null;
        HttpStatus status = HttpStatus.BAD_REQUEST;

        try {
            picture = fileService.getPictureById(pictureId);
            status = HttpStatus.ACCEPTED;
        } catch (IOException e) {
            logger.trace("Non Existing file ID: " + pictureId);
        }
        return ResponseEntity.status(status).body(picture);
    }
}
