package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book save(Book book);

    void deleteByTitleAndAuthor(Book book);

    List<Book> getAll();

    List<Book> getByTitle(String title);

    List<Book> getByAuthor(Author author);

    List<Book> getByGenre(Genre genre);

    Optional<Book> getByTitleAndAuthor(Book book);

    void deleteBookByAuthor(String name, String surname);

}
