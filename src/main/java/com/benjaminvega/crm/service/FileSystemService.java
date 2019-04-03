package com.benjaminvega.crm.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class FileSystemService {

    public void store(MultipartFile file, String path) throws Exception {
        Files.createDirectories(Paths.get(path));
        Files.copy(file.getInputStream(), Paths.get(path).resolve(file.getOriginalFilename()), REPLACE_EXISTING);
    }

    public void move(String sourceFile, String destinationPath, String filename) throws IOException {
        Files.createDirectories(Paths.get(destinationPath));
        Files.move(Paths.get(sourceFile), Paths.get(destinationPath + filename), REPLACE_EXISTING);
    }

    public Resource getFile(String path) throws MalformedURLException {
        Resource resource = new UrlResource(Paths.get(path).toUri());
        if (!(resource.exists() || resource.isReadable())) {
            throw new RuntimeException("FAIL Because of resource!");
        }
        return resource;
    }
}