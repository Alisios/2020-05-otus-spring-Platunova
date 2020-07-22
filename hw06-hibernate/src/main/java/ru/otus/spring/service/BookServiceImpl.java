package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.DbException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    final private BookDao bookDao;

    @Override
    @Transactional
    public Book create(Book book) {
        try {
            Optional<Book> b = bookDao.findByTitleAndAuthor(book);
            return b.orElseGet(() -> bookDao.insert(book));
        } catch (Exception ex) {
            log.error("Error with inserting book with title {}, {}", book.getTitle(), ex.getCause());
            throw new DbException("Error with inserting book with title " + book.getTitle(), ex);
        }
    }

    @Override
    @Transactional
    public void saveByTitle(Book book) {
        try {
            bookDao.updateByTitle(book);
        } catch (Exception ex) {
            log.error("Error with saving book with title {}, {}", book.getTitle(), ex.getCause());
            throw new DbException("Error with saving book with title " + book.getTitle(), ex);
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            bookDao.deleteById(id);
        } catch (Exception ex) {
            log.error("Error  with deleting book with id {}, {}", id, ex.getCause());
            throw new DbException("Error with deleting book with id " + id, ex);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        try {
            return bookDao.findAll();
        } catch (Exception ex) {
            log.error("Error with finding all books: {}", ex.getCause());
            throw new DbException("Error with finding all books", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getById(long id) {
        try {
            return bookDao.findById(id);
        } catch (Exception ex) {
            log.error("Error with finding book by id {}, {}", id, ex.getCause());
            throw new DbException("Error with finding book by id" + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByAuthor(Author author) {
        try {
            return bookDao.findByAuthor(author);
        } catch (Exception ex) {
            log.error("Error with finding book by author {} {}, {}", author.getName(), author.getSurname(), ex.getCause());
            throw new DbException("Error with finding book by author" + author.getName() + " " + author.getSurname(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByGenre(Genre genre) {
        try {
            return bookDao.findByGenre(genre);
        } catch (Exception ex) {
            log.error("Error with finding book by genre {}, {}" + genre.getType(), ex.getCause());
            throw new DbException("Error with finding book by genre" + genre.getType(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getByTitleAndAuthor(Book book) {
        try {
            return bookDao.findByTitleAndAuthor(book);
        } catch (Exception ex) {
            log.error("Error with finding book by by author and title {}, {}" + book.getTitle(), ex.getCause());
            throw new DbException("Error with finding book by author and title" + book.getTitle(), ex);
        }

    }

}
