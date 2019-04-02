package com.benjaminvega.crm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class FileSystemServiceTest {

    @InjectMocks
    private FileSystemService cut;

    @Test(expected = RuntimeException.class)
    public void getNonExistingFile() throws IOException {
        cut.getFile("/random/directory/andFile.jpg");
    }

    @Test
    public void getExistingFile() throws IOException, URISyntaxException {
        String name = "customerProfile.png";
        Resource resource = cut.getFile(Paths.get(ClassLoader.getSystemResource(name).toURI()).toString());
        assertNotNull(resource);
    }
}
