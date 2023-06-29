package com.example.restservice.model;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "games")
public class Game extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "release_date", columnDefinition = "DATE")
    private Date releaseDate;
    @ManyToOne
    private Developer dev;
    @Column(name = "description")
    private String description;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "games_tags", joinColumns = {@JoinColumn(name = "game_id")}, inverseJoinColumns = {@JoinColumn(name = "tag_id")})
//    private Set<Tag> tags;

    public Developer getDev() {
        return dev;
    }

    public void setDev(Developer dev) {
        this.dev = dev;
    }

//    public Set<Tag> getTags() {
//        return tags;
//    }
//
//    public void setTags(Set<Tag> tags) {
//        this.tags = tags;
//    }

//    public Long getDevId() {
//        return devId;
//    }

//    public void setDevId(Long devId) {
//        this.devId = devId;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
}
