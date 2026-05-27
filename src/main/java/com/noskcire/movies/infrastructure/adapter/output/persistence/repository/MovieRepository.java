package com.noskcire.movies.infrastructure.adapter.output.persistence.repository;

import com.noskcire.movies.domain.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    Page<Movie> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );

    @Query("""
            SELECT m
            FROM Movie m
            WHERE
                (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
            AND
                (:gender IS NULL OR LOWER(m.gender) = LOWER(:gender))
            """)
    Page<Movie> searchMovies(
            @Param("title") String title,
            @Param("gender") String gender,
            Pageable pageable

    );

}
