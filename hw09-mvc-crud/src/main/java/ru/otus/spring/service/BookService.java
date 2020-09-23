package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(Book book);

    void deleteById(long id);

    List<Book> getAll();

    Optional<Book> getById(long id);

    List<Book> getByAuthor(Author author);

    List<Book> getByGenre(Genre genre);

    Optional<Book> getByTitleAndAuthor(Book book);
}
