package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    final private BookRepository bookRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book save(Book book) {
        try {
            Optional<Book> b = bookRepository.findFirstByTitleAndAuthor_NameAndAuthor_Surname(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname());
            return b.orElseGet(() -> bookRepository.save(book));
        } catch (RuntimeException ex) {
            log.error("Error with saving book:  {}", ex.getCause());
            throw new DbException("Error with saving book", ex);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTitleAndAuthor(Book book) {
        try {
            bookRepository.deleteBookByTitleAndAuthor(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname());
        } catch (RuntimeException ex) {
            log.error("Error  with deleting book with title \"{}\" and author {} {}, {}", book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname(), ex.getCause());
            throw new DbException("Error with deleting book with title \"" + book.getTitle() + "\" and author " + book.getAuthor().getName() + " " + book.getAuthor().getSurname(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBookByAuthor(String name, String surname) {
        try {
            bookRepository.deleteBooksByAuthor(name, surname);
        } catch (RuntimeException ex) {
            log.error("Error  with deleting book with author {} {}, {}", name, surname, ex.getCause());
            throw new DbException("Error with deleting book with author " + name + " " + surname, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Book> getAll() {
        try {
            return bookRepository.findAll();
        } catch (RuntimeException ex) {
            log.error("Error with finding all books: {}", ex.getCause());
            throw new DbException("Error with finding all books", ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Book> getByTitle(String title) {
        try {
            return bookRepository.findAllByTitle(title);
        } catch (RuntimeException ex) {
            log.error("Error with finding book by title {}, {}", title, ex.getCause());
            throw new DbException("Error with finding book by id" + title, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Book> getByAuthor(Author author) {
        try {
            return bookRepository.findFirstByAuthor_NameAndAuthor_Surname(author.getName(), author.getSurname());
        } catch (RuntimeException ex) {
            log.error("Error with finding book by author {} {}, {}", author.getName(), author.getSurname(), ex.getCause());
            throw new DbException("Error with finding book by author" + author.getName() + " " + author.getSurname(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Book> getByGenre(Genre genre) {
        try {
            return bookRepository.findAllByGenre_Name(genre.getName());
        } catch (RuntimeException ex) {
            log.error("Error with finding book by genre {}, {}" + genre.getName(), ex.getCause());
            throw new DbException("Error with finding book by genre" + genre.getName(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Book> getByTitleAndAuthor(Book book) {
        try {
            return bookRepository.findFirstByTitleAndAuthor_NameAndAuthor_Surname(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname());
        } catch (RuntimeException ex) {
            log.error("Error with finding book by by author and title {}, {}" + book.getTitle(), ex.getCause());
            throw new DbException("Error with finding book by author and title" + book.getTitle(), ex);
        }

    }

}
