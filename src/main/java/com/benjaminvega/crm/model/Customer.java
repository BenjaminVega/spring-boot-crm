package com.benjaminvega.crm.model;

import lombok.*;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@Table(schema = "crm")
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String surname;

    @Column
    private long pictureId;

    @Column
    @NotNull
    private long editorId;

    @Tolerate
    public Customer() {}

}
