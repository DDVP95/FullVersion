package com.chessgg.chessapp.maven.controller;

import com.chessgg.chessapp.maven.model.Puzzle;
import com.chessgg.chessapp.maven.model.Theme;
import com.chessgg.chessapp.maven.service.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PuzzleController {

    @Autowired
    private PuzzleService puzzleService;

    @GetMapping("/puzzles")
    public ResponseEntity<List<Puzzle>> getAllPuzzles() {
        List<Puzzle> puzzles = puzzleService.getAllPuzzles();
        return ResponseEntity.ok(puzzles);
    }

    @GetMapping("/daily-puzzle")
    public ResponseEntity<?> getDailyPuzzle() {
        LocalDate today = LocalDate.now();
        Optional<Puzzle> dailyPuzzle = puzzleService.getDailyPuzzle(today);
        if (dailyPuzzle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "No daily puzzle found."));
        }
        Puzzle puzzle = dailyPuzzle.get();
        List<List<String>> solutions = new ArrayList<>();
        if (puzzle.getSolutions() != null) {
            for (var solution : puzzle.getSolutions()) {
                List<String> moves = new ArrayList<>();
                solution.getMoves().forEach(move -> moves.add(move.getMoveText()));
                solutions.add(moves);
            }
        }
        List<String> themeNames = puzzle.getThemes().stream()
                .map(Theme::getName)
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("id", puzzle.getId());
        response.put("position", puzzle.getPosition());
        response.put("description", puzzle.getDescription());
        response.put("rating", puzzle.getRating());
        response.put("title", puzzle.getTitle());
        response.put("themes", themeNames);
        response.put("pgn", puzzle.getPgn());
        response.put("ratingDeviation", puzzle.getRatingDeviation());
        response.put("popularity", puzzle.getPopularity());
        response.put("nbPlays", puzzle.getNbPlays());
        response.put("gameUrl", puzzle.getGameUrl());
        response.put("video", puzzle.getVideo());
        response.put("openingTags", puzzle.getOpeningTags());
        response.put("daily", puzzle.isDaily());
        response.put("publishDate", puzzle.getPublishDate());
        response.put("solutions", solutions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/random-puzzle")
    public ResponseEntity<?> getRandomPuzzle() {
        Optional<Puzzle> randomPuzzle = puzzleService.getRandomPuzzle();
        if (randomPuzzle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "No random puzzle found."));
        }
        Puzzle puzzle = randomPuzzle.get();
        Map<String, Object> response = puzzleService.buildPuzzleResponse(puzzle);
        return ResponseEntity.ok(response);
    }
}
