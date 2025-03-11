package com.chessgg.chessapp.maven.repository;

import com.chessgg.chessapp.maven.model.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {
    Optional<Puzzle> findByPublishDateAndIsDaily(LocalDate publishDate, boolean isDaily);

    Optional<Puzzle> findFirstByOrderByPublishDateDesc();

    Optional<Puzzle> findByPosition(String position);

    Optional<Puzzle> findFirstByPublishDateLessThanOrderByPublishDateDesc(LocalDate date);

    List<Puzzle> findByIsDaily(boolean isDaily);
}
