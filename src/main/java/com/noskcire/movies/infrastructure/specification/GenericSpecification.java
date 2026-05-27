package com.noskcire.movies.infrastructure.specification;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecification {

    public static <T> Specification<T> contains(
            String field,
            String value
    ) {
        return (root, query, cb) ->
                value == null || value.isBlank()
                ? null
                : cb.like(
                        cb.lower(root.get(field).as(String.class)),
                        "%" + value.toLowerCase() + "%"
                );
    }

    public static <T> Specification<T> equalsTo(
            String field,
            Object value
    ) {
        return (root, query, cb) ->
                value == null
                ? null
                : cb.equal(
                        root.get(field),
                        value
                );
    }

}
