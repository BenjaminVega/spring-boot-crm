package com.benjaminvega.crm.controller;

import com.benjaminvega.crm.model.Customer;
import com.benjaminvega.crm.model.CustomerView;
import com.benjaminvega.crm.model.FileView;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(properties = "crm.filesystem.basePath=/tmp/crm/")
public class ControllerIT {

    @Autowired
    public Controller cut;

    private static long pictureId;
    private static long customerId;
    private static int numberOfCustomers;

    @Before
    public void setup() {

    }

    @Test
    public void a_firstUploadCustomerPicture() throws URISyntaxException {
        MultipartFile profilePicture = getPictureFromResourceFolder();
        ResponseEntity<FileView> fileViewResponseEntity = cut.uploadNewPicture(profilePicture);

        assertThat(fileViewResponseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        this.pictureId = fileViewResponseEntity.getBody().getId();
    }

    @Test
    public void b_secondCreateNewCustomerSuccess() throws IOException {
        CustomerView customerView = CustomerView.builder()
                .name("Michael")
                .surname("Jackson")
                .pictureId(pictureId)
                .build();

        ResponseEntity<Customer> customerResponse = cut.postNewCustomer(customerView);
        assertThat(customerResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        Customer actualCustomer = customerResponse.getBody();
        customerId = actualCustomer.getId();

        assertThat(actualCustomer.getId()).isGreaterThan(0L);
        assertThat(actualCustomer.getName()).isEqualTo(customerView.getName());
        assertThat(actualCustomer.getSurname()).isEqualTo(customerView.getSurname());

    }

    @Test
    public void c_thirdGetFirstCustomerAddedInDb() {
        ResponseEntity<Customer> customerResponse = cut.getCustomerById(customerId);
        assertThat(customerResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void d_fourthGetPictureById() {
        ResponseEntity<Resource>  pictureResponse = cut.getPictureById(pictureId);

        assertThat(pictureResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void e_updateCustomerDetails() {
        String newSurname = "Riot";
        ResponseEntity<Customer> customerResponse = cut.getCustomerById(customerId);

        CustomerView customerView = CustomerView.builder()
                .name(customerResponse.getBody().getName())
                .surname(newSurname)
                .pictureId(customerResponse.getBody().getPictureId())
                .build();

        ResponseEntity<Customer> customerUpdateResponse = cut.updateCustomer(customerView,customerId);

        assertThat(customerUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(customerUpdateResponse.getBody().getSurname()).isEqualTo(newSurname);
    }

    @Test
    public void f_listAllCustomers() {
        ResponseEntity<List<Customer>> customers  = cut.listCustomers();
        assertThat(customers.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        numberOfCustomers = customers.getBody().size();
    }

    @Test
    public void g_deleteCustomer() {
        ResponseEntity<Void> response  = cut.deleteCustomer(customerId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ResponseEntity<List<Customer>> customers  = cut.listCustomers();
        int newNumberOfCustomers = customers.getBody().size();
        assertThat(numberOfCustomers -1).isEqualTo(newNumberOfCustomers);
        numberOfCustomers = newNumberOfCustomers;
    }

    @Test(expected = ConstraintViolationException.class)
    public void postNewCustomerWithInvalidStructure() throws IOException {
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

    @Test
    public void createNewCustomerWithoutPicture() throws IOException {
        CustomerView customerView = CustomerView.builder()
                .name("Michael")
                .surname("Jackson")
                .build();

        ResponseEntity<Customer> customerResponse = cut.postNewCustomer(customerView);
        assertThat(customerResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        Customer actualCustomer = customerResponse.getBody();

        assertThat(actualCustomer.getId()).isGreaterThan(0L);
        assertThat(actualCustomer.getName()).isEqualTo(customerView.getName());
        assertThat(actualCustomer.getSurname()).isEqualTo(customerView.getSurname());

    }

    @Test
    public void errorUploadingCustomerPicture() {
        ResponseEntity<FileView> fileViewResponseEntity = cut.uploadNewPicture(null);

        assertThat(fileViewResponseEntity.getStatusCode()).isEqualTo(HttpStatus.EXPECTATION_FAILED);
    }


    private MultipartFile getPictureFromResourceFolder() throws URISyntaxException {
        String name = "customerProfile.png";
        Path path = Paths.get(ClassLoader.getSystemResource(name).toURI());
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            fail(e.getMessage());
        }

        return new MockMultipartFile(name, name, "image/png", content);
    }
}
