package com.example.restservice.dto;

import java.util.Set;

public class DevDTO extends BaseDTO {
    private String name;
    private Set<Long> gameIds;

    public Set<Long> getGameIds() {
        return gameIds;
    }

    public void setGameIds(Set<Long> gameIds) {
        this.gameIds = gameIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DevDTO{" + "id=" + this.getId() + ", name='" + name + '\'' + '}';
    }
}
