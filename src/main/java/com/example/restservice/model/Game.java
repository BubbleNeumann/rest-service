package com.example.restservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "Games")
public class Game extends BaseEntity {
    @Column(name = "title")
    private String title;
    @Column(name = "release_date", columnDefinition = "DATE")
    private Date releaseDate;

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Game{" + "title='" + title + '\'' + ", releaseDate=" + releaseDate + '}';
    }
}
