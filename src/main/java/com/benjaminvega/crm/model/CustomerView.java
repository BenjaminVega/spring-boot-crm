package com.benjaminvega.crm.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CustomerView {
    @NotNull
    private String name;
    @NotNull
    private String surname;
    private long pictureId;
    @NotNull
    private long editorId;

    @Tolerate
    public CustomerView() {}
}
