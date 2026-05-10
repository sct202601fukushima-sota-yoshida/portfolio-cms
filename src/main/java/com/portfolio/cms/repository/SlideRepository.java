package com.portfolio.cms.repository;

import com.portfolio.cms.entity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SlideRepository extends JpaRepository<Slide, Long> {

    @Query("SELECT DISTINCT s FROM Slide s LEFT JOIN FETCH s.category c LEFT JOIN FETCH s.images WHERE s.deletedAt IS NULL ORDER BY c.sortOrder ASC, s.sortOrder ASC, s.id ASC")
    List<Slide> findAllNotDeleted();

    @Query("SELECT DISTINCT s FROM Slide s LEFT JOIN FETCH s.category c LEFT JOIN FETCH s.images WHERE s.isActive = true AND s.deletedAt IS NULL ORDER BY c.sortOrder ASC, s.sortOrder ASC, s.id ASC")
    List<Slide> findAllActive();

    @Query("SELECT DISTINCT s FROM Slide s LEFT JOIN FETCH s.category c LEFT JOIN FETCH s.images WHERE s.isActive = true AND s.deletedAt IS NULL AND s.category.id = :categoryId ORDER BY s.sortOrder ASC, s.id ASC")
    List<Slide> findAllActiveByCategoryId(Long categoryId);

    @Query("SELECT DISTINCT s FROM Slide s LEFT JOIN FETCH s.category LEFT JOIN FETCH s.images WHERE s.id = :id AND s.deletedAt IS NULL")
    Optional<Slide> findByIdNotDeleted(Long id);
}
