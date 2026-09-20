package com.dslion.netflix_clone.genre.repository;

import com.dslion.netflix_clone.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    boolean existsByName(String name);
}
