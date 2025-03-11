package com.chessgg.chessapp.maven.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    // Self-referential relationships for hierarchy (optional)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Theme parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Theme> subthemes = new HashSet<>();

    @ManyToMany(mappedBy = "themes")
    private Set<Puzzle> puzzles = new HashSet<>();

    // No-argument constructor
    public Theme() {}

    // Constructor that takes a name
    public Theme(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Theme getParent() { return parent; }
    public void setParent(Theme parent) { this.parent = parent; }

    public Set<Theme> getSubthemes() { return subthemes; }
    public void setSubthemes(Set<Theme> subthemes) { this.subthemes = subthemes; }

    public Set<Puzzle> getPuzzles() { return puzzles; }
    public void setPuzzles(Set<Puzzle> puzzles) { this.puzzles = puzzles; }
}
