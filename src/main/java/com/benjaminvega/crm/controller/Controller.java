package com.benjaminvega.crm.controller;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.CustomerView;
import com.benjaminvega.crm.model.File;
import com.benjaminvega.crm.model.FileView;
import com.benjaminvega.crm.service.CustomerService;
import com.benjaminvega.crm.service.FileService;
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

@RestController
@RequestMapping
public class Controller {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ConversionService conversionService;

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") long customerId) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Customer customer = customerService.getCustomerByCustomerId(customerId);

        if (customer != null ){
            status = HttpStatus.ACCEPTED;
        }

        return ResponseEntity.status(status).body(customer);
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> postNewCustomer(@RequestBody @Valid CustomerView customer ) throws IOException {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Customer customerResponse = customerService.createNewCustomer(
                conversionService.convert(customer, Customer.class)
        );
        if (customerResponse != null ){
            if (customer.getPictureId() != 0L) {
                fileService.updatePicture(customer.getPictureId(),customerResponse.getId());
            }
            status = HttpStatus.ACCEPTED;
        }

        return ResponseEntity.status(status).body(customerResponse);
    }

    @PostMapping("/pictures")
    public ResponseEntity<FileView> uploadNewPicture(@RequestParam("file") MultipartFile picture) {
        try {
            File file = fileService.addPicture(picture);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new FileView(file.getId()));
        } catch (Exception e) {
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
        } catch (IOException e){
            System.out.println("Non Existing ID");
        }
        return ResponseEntity.status(status).body(picture);
    }
}
