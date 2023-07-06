package com.example.restservice.dto;

public class DevDTO extends BaseDTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DevDTO{" +
                "id=" + this.getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
