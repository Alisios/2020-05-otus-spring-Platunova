package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book insert(Book book);

    void updateByTitle(Book book);

    void deleteById(long id);

    List<Book> findAll();

    Optional<Book> findById(long id);

    List<Book> findByGenre(Genre genre);

    List<Book> findByAuthor(Author author);

    Optional<Book> findByTitleAndAuthor(Book book);

    long count();


}
