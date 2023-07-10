package com.example.restservice.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "games")
public class Game extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "release_date", columnDefinition = "DATE")
    private Date releaseDate;
    @ManyToOne
    @JoinColumn(name="dev_id", nullable=false)
    private Developer dev;
    @Column(name = "description")
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "games_tags",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "tags_id")}
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Tag> tags = new HashSet<>();

    public Developer getDev() {
        return dev;
    }

    public void setDev(Developer dev) {
        this.dev = dev;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

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

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    @Override
    public String toString() {
        return "Game{" +
                "title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", dev=" + dev +
                ", description='" + description +
                "'}";
    }
}
