package com.example.restservice.model;

import jakarta.persistence.*;


@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
