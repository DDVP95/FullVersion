package com.chessgg.chessapp.maven.serviceImpl;

import com.chessgg.chessapp.maven.model.Puzzle;
import com.chessgg.chessapp.maven.repository.PuzzleRepository;
import com.chessgg.chessapp.maven.service.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PuzzleServiceImpl implements PuzzleService {

    @Autowired
    private PuzzleRepository puzzleRepository;

    @Override
    public Optional<Puzzle> getDailyPuzzle(LocalDate today) {
        Optional<Puzzle> dailyPuzzle = puzzleRepository.findByPublishDateAndIsDaily(today, true);
        if (dailyPuzzle.isEmpty()) {
            return findPreviousDayPuzzle(today);
        }
        return dailyPuzzle;
    }

    private Optional<Puzzle> findPreviousDayPuzzle(LocalDate date) {
        LocalDate searchDate = date.minusDays(1);
        while (searchDate.isAfter(LocalDate.now().minusYears(1))) {
            Optional<Puzzle> previousPuzzle = puzzleRepository.findByPublishDateAndIsDaily(searchDate, true);
            if (previousPuzzle.isPresent()) {
                return previousPuzzle;
            }
            searchDate = searchDate.minusDays(1);
        }
        return Optional.empty();
    }

    @Override
    public List<Puzzle> getAllPuzzles() {
        return puzzleRepository.findAll();
    }

    @Override
    public Puzzle getPuzzleById(Long id) {
        return puzzleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puzzle not found with ID: " + id));
    }

    @Override
    public List<Puzzle> getPuzzlesByDateRange(LocalDate startDate, LocalDate endDate) {
        return puzzleRepository.findAll().stream()
                .filter(p -> !p.getPublishDate().isBefore(startDate) && !p.getPublishDate().isAfter(endDate))
                .toList();
    }

    @Override
    public Optional<Puzzle> getMostRecentPuzzleBeforeDate(LocalDate date) {
        return puzzleRepository.findFirstByPublishDateLessThanOrderByPublishDateDesc(date);
    }

    @Override
    public List<Puzzle> getDailyPuzzles() {
        return puzzleRepository.findByIsDaily(true);
    }

    @Override
    public Puzzle savePuzzle(Puzzle puzzle) {
        return puzzleRepository.save(puzzle);
    }

    @Override
    public Puzzle updatePuzzle(Long id, Puzzle updatedPuzzle) {
        return puzzleRepository.findById(id).map(existingPuzzle -> {
            if (updatedPuzzle.getPosition() != null) {
                existingPuzzle.setPosition(updatedPuzzle.getPosition());
            }
            if (updatedPuzzle.getSolutions() != null) {
                existingPuzzle.setSolutions(updatedPuzzle.getSolutions());
            }
            if (updatedPuzzle.getDescription() != null) {
                existingPuzzle.setDescription(updatedPuzzle.getDescription());
            }
            if (updatedPuzzle.getPublishDate() != null) {
                existingPuzzle.setPublishDate(updatedPuzzle.getPublishDate());
            }
            existingPuzzle.setDaily(updatedPuzzle.isDaily());
            return puzzleRepository.save(existingPuzzle);
        }).orElseThrow(() -> new RuntimeException("Puzzle not found with ID: " + id));
    }

    @Override
    public void deletePuzzle(Long id) {
        if (!puzzleRepository.existsById(id)) {
            throw new RuntimeException("Puzzle not found with ID: " + id);
        }
        puzzleRepository.deleteById(id);
    }

    // NEW METHOD: getRandomPuzzle() returns a random puzzle from those marked daily.
    @Override
    public Optional<Puzzle> getRandomPuzzle() {
        List<Puzzle> puzzles = puzzleRepository.findByIsDaily(true);
        if (puzzles.isEmpty()) {
            return Optional.empty();
        }
        Random random = new Random();
        Puzzle randomPuzzle = puzzles.get(random.nextInt(puzzles.size()));
        return Optional.of(randomPuzzle);
    }

    // NEW METHOD: buildPuzzleResponse converts a Puzzle entity to a Map response.
    @Override
    public Map<String, Object> buildPuzzleResponse(Puzzle puzzle) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", puzzle.getId());
        response.put("position", puzzle.getPosition());
        response.put("description", puzzle.getDescription());
        response.put("rating", puzzle.getRating());
        response.put("title", puzzle.getTitle());
        List<String> themeNames = puzzle.getThemes().stream()
                .map(theme -> theme.getName())
                .collect(Collectors.toList());
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
        List<List<String>> solutions = new ArrayList<>();
        if (puzzle.getSolutions() != null) {
            puzzle.getSolutions().forEach(solution -> {
                List<String> moves = new ArrayList<>();
                solution.getMoves().forEach(move -> moves.add(move.getMoveText()));
                solutions.add(moves);
            });
        }
        response.put("solutions", solutions);
        return response;
    }
}
