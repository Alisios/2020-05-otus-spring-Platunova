package ru.otus.spring.service;

import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Author save(Author author);

    void deleteById(long id);

    List<Author> getAll();

    Optional<Author> getById(long id);

    Optional<Author> getByName(String name, String surname);

}

