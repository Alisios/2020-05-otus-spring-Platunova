package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorService {

    Author save(Author author);

    List<Author> getAll();

    List<String> getBookTitlesByAuthor(String name, String surname);

    void deleteByNameAndSurname(String name, String surname);

}

