package com.chessgg.chessapp.maven.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "puzzle")
@JsonDeserialize(using = PuzzleDeserializer.class) // Use our custom deserializer
public class Puzzle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;
    private String description;
    private int rating;
    private String title;
    private String pgn;
    private int ratingDeviation;
    private int popularity;
    private int nbPlays;
    private String gameUrl;
    private String video;
    private String openingTags;

    @JsonProperty("daily")
    private boolean isDaily;

    private LocalDate publishDate;

    @OneToMany(mappedBy = "puzzle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PuzzleSolution> solutions = new ArrayList<>();

    // New many-to-many mapping for themes
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "puzzle_theme",
        joinColumns = @JoinColumn(name = "puzzle_id"),
        inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private Set<Theme> themes = new HashSet<>();

    // Getters and Setters
    // ... (include getters and setters for all fields, including themes)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPgn() { return pgn; }
    public void setPgn(String pgn) { this.pgn = pgn; }

    public int getRatingDeviation() { return ratingDeviation; }
    public void setRatingDeviation(int ratingDeviation) { this.ratingDeviation = ratingDeviation; }

    public int getPopularity() { return popularity; }
    public void setPopularity(int popularity) { this.popularity = popularity; }

    public int getNbPlays() { return nbPlays; }
    public void setNbPlays(int nbPlays) { this.nbPlays = nbPlays; }

    public String getGameUrl() { return gameUrl; }
    public void setGameUrl(String gameUrl) { this.gameUrl = gameUrl; }

    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }

    public String getOpeningTags() { return openingTags; }
    public void setOpeningTags(String openingTags) { this.openingTags = openingTags; }

    public boolean isDaily() { return isDaily; }
    public void setDaily(boolean daily) { isDaily = daily; }

    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }

    public List<PuzzleSolution> getSolutions() { return solutions; }
    public void setSolutions(List<PuzzleSolution> solutions) { this.solutions = solutions; }

    public Set<Theme> getThemes() { return themes; }
    public void setThemes(Set<Theme> themes) { this.themes = themes; }
}
