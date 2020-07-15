package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.DbException;
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
            Optional<Book> b = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname());
            return b.orElseGet(() -> bookRepository.save(book));
        } catch (RuntimeException ex) {
            log.error("Error with saving book with title {}, {}", book.getTitle(), ex.getCause());
            throw new DbException("Error with saving book with title " + book.getTitle(), ex);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        try {
            bookRepository.deleteById(id);
        } catch (RuntimeException ex) {
            log.error("Error  with deleting book with id {}, {}", id, ex.getCause());
            throw new DbException("Error with deleting book with id " + id, ex);
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
    public Optional<Book> getById(long id) {
        try {
            return bookRepository.findById(id);
        } catch (RuntimeException ex) {
            log.error("Error with finding book by id {}, {}", id, ex.getCause());
            throw new DbException("Error with finding book by id" + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Book> getByAuthor(Author author) {
        try {
            return bookRepository.findByAuthor(author.getName(), author.getSurname());
        } catch (RuntimeException ex) {
            log.error("Error with finding book by author {} {}, {}", author.getName(), author.getSurname(), ex.getCause());
            throw new DbException("Error with finding book by author" + author.getName() + " " + author.getSurname(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Book> getByGenre(Genre genre) {
        try {
            return bookRepository.findByGenre(genre.getType());
        } catch (RuntimeException ex) {
            log.error("Error with finding book by genre {}, {}" + genre.getType(), ex.getCause());
            throw new DbException("Error with finding book by genre" + genre.getType(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Book> getByTitleAndAuthor(Book book) {
        try {
            return bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname());
        } catch (RuntimeException ex) {
            log.error("Error with finding book by by author and title {}, {}" + book.getTitle(), ex.getCause());
            throw new DbException("Error with finding book by author and title" + book.getTitle(), ex);
        }

    }

}
