package com.example.restservice.dto;


import java.util.Date;
import java.util.Set;

public class GameDTO extends BaseDTO {
    private String title;
    private Date releaseDate;
    private Long devId;
    private String description;
    private Set<Long> tagIds;

    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getDevId() {
        return devId;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GameDTO{" +
                "id=" + this.getId() +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", devId=" + devId +
                ", description='" + description + '\'' + '}';
    }
}
