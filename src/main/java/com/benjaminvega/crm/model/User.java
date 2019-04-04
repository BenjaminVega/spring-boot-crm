package com.benjaminvega.crm.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class User {
    @NotNull
    private String username;

    private String firstName;

    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private List<String> roles;

    @Tolerate
    public User() {}
}
