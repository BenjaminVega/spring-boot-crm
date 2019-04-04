package com.benjaminvega.crm.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Setter
@Getter
public class CustomerResponse {
    private long id;
    private String name;
    private String surname;
    private String pictureUrl;
    private UUID editorId;
}
