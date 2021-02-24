package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    Genre save(Genre genre);

    void deleteById(long id);

    List<Genre> getAll();

    Optional<Genre> getById(long id);

    Optional<Genre> getByType(String type);

}
