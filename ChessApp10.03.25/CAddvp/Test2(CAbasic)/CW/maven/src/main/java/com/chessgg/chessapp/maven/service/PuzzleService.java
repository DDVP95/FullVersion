package com.chessgg.chessapp.maven.service;

import com.chessgg.chessapp.maven.model.Puzzle;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PuzzleService {

    Optional<Puzzle> getDailyPuzzle(LocalDate today);

    List<Puzzle> getAllPuzzles();

    Puzzle getPuzzleById(Long id);

    List<Puzzle> getPuzzlesByDateRange(LocalDate startDate, LocalDate endDate);

    Optional<Puzzle> getMostRecentPuzzleBeforeDate(LocalDate date);

    List<Puzzle> getDailyPuzzles();

    Puzzle savePuzzle(Puzzle puzzle);

    Puzzle updatePuzzle(Long id, Puzzle puzzle);

    void deletePuzzle(Long id);

    // NEW METHODS:
    Optional<Puzzle> getRandomPuzzle();
    Map<String, Object> buildPuzzleResponse(Puzzle puzzle);
}
