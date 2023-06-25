package com.example.restservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(RestServiceApplication.class).run(args);
    }

    /**
     * Used for the automatic conversion from db entities objects to entity DTOs.
     * Called from convertToDTO() & convertToEntity() in corresponding controllers.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
