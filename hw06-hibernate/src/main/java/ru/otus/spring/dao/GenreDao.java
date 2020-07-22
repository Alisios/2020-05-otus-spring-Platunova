package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    Genre insert(Genre book);

    void update(Genre book);

    void deleteById(long id);

    List<Genre> findAll();

    Optional<Genre> findById(long id);

    Optional<Genre> findByType(String type);

    long count();
}
