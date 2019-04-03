package com.benjaminvega.crm.service;


import com.benjaminvega.crm.model.File;
import com.benjaminvega.crm.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Component
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileSystemService fileSystemService;

    @Value("${crm.filesystem.basePath}")
    private String fileSystemBasePath;

    public File addPicture(MultipartFile picture) throws Exception {
        Optional<File> previousFile = fileRepository.findFirstByOrderByIdDesc();
        long id = previousFile.map(file1 -> file1.getId() + 1).orElse(1L);

        File file = File.builder()
                .id(id)
                .path(fileSystemBasePath)
                .name(picture.getOriginalFilename())
                .build();

        fileSystemService.store(picture, fileSystemBasePath);

        return fileRepository.save(file);
    }

    public File updatePicture(long fileId, long customerId) throws IOException {
        Optional<File> file = fileRepository.findById(fileId);
        if(file.isPresent()) {
            fileSystemService.move(getAbsolutePathOfAPicture(file.get()),
                    getUpdatedPathOfAPicture(file.get(), customerId),
                    file.get().getName());

            file.get().setCustomerId(customerId);
            file.get().setPath(getUpdatedPathOfAPicture(file.get(), customerId));
            return fileRepository.save(file.get());
        } else {
          return null;
        }
    }

    public Resource getPictureById(long fileId) throws IOException {
        Optional<File> file = fileRepository.findById(fileId);
        if(file.isPresent()) {
            return fileSystemService.getFile(getAbsolutePathOfAPicture(file.get()));
        } else {
            return null;
        }
    }

    private String getAbsolutePathOfAPicture(File file) {
        return file.getPath() + file.getName();
    }

    private String getUpdatedPathOfAPicture(File file, long customerId) {
        return file.getPath() + customerId + "/";
    }

}
