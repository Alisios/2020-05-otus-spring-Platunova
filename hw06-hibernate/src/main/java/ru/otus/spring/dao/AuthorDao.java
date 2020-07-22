package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    Author insert(Author book);

    void update(Author book);

    void deleteById(long id);

    List<Author> findAll();

    Optional<Author> findById(long id);

    Optional<Author> findByFullName(String name, String surname);

    long count();

}
