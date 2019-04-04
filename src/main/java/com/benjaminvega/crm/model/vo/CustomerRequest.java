package com.benjaminvega.crm.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CustomerRequest {

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private long pictureId;

    @Tolerate
    public CustomerRequest() {}
}
