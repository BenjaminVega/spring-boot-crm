package com.benjaminvega.crm.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "files")
public class File {
    @Id
    private long id;

    private String name;

    private String path;

    private long customerId;

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
