package com.example.restservice.dto;

public class TagDTO extends BaseDTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TagDTO{" +
                "id=" + this.getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
