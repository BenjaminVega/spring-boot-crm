package com.benjaminvega.crm.repository;

import com.benjaminvega.crm.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FileRepository extends MongoRepository<File, Long> {
    Optional<File> findFirstByOrderByIdDesc();
}
