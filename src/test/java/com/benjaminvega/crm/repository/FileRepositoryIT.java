package com.benjaminvega.crm.repository;

import com.benjaminvega.crm.model.File;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileRepositoryIT {

    @Autowired
    private FileRepository fileRepository;
    private File file;

    @Before
    public void setUp() {
        file = File.builder()
                .name("miniProfilePortrait")
                .path("/tmp/crm")
                .build();
    }

    @Test
    public void firstAddAndReadFromMongoDb() {


    }
}
