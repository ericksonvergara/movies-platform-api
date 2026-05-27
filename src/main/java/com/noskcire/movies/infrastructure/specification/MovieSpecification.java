package com.noskcire.movies.infrastructure.specification;

import com.noskcire.movies.domain.model.Movie;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {

    public static Specification<Movie> hasTitle(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                        ? null
                        : cb.like(
                                cb.lower(root.get("title")),
                             "%" + title.toLowerCase() + "%"
                        );
    }

    public static Specification<Movie> hasGender(String gender) {
        return (root, query, cb) ->
                gender == null || gender.isBlank()
                        ? null
                        : cb.equal(
                                cb.lower(root.get("gender")),
                                gender.toLowerCase()
                );

    }

    public static Specification<Movie> hasreleaseYear(Integer releaseYear) {
        return (root, query, cb) ->
                releaseYear == null
                            ? null
                            : cb.equal(
                                    root.get("releaseYear"),
                                    releaseYear
                );
    }
}
