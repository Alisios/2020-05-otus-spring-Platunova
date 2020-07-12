package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.DaoException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DbServiceBookImpl implements DbServiceBook {

    final private BookDao bookDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book create(Book book) {
        try {
            Optional<Book> b = bookDao.findByTitleAndAuthor(book);
            return b.orElseGet(() -> bookDao.insert(book));
        } catch (RuntimeException ex) {
            log.error("Error with inserting book with title {}, {}", book.getTitle(), ex.getCause());
            throw new DaoException("Error with inserting book with title " + book.getTitle(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByTitle(Book book) {
        try {
            bookDao.updateByTitle(book);
        } catch (RuntimeException ex) {
            log.error("Error with saving book with title {}, {}", book.getTitle(), ex.getCause());
            throw new DaoException("Error with saving book with title " + book.getTitle(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        try {
            bookDao.deleteById(id);
        } catch (RuntimeException ex) {
            log.error("Error  with deleting book with id {}, {}", id, ex.getCause());
            throw new DaoException("Error with deleting book with id " + id, ex);
        }

    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Book> getAll() {
        try {
            return bookDao.findAll();
        } catch (RuntimeException ex) {
            log.error("Error with finding all books: {}", ex.getCause());
            throw new DaoException("Error with finding all books", ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Book> getById(long id) {
        try {
            return bookDao.findById(id);
        } catch (RuntimeException ex) {
            log.error("Error with finding book by id {}, {}", id, ex.getCause());
            throw new DaoException("Error with finding book by id" + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Book> getByAuthor(Author author) {
        try {
            return bookDao.findByAuthor(author);
        } catch (RuntimeException ex) {
            log.error("Error with finding book by author {} {}, {}", author.getName(), author.getSurname(), ex.getCause());
            throw new DaoException("Error with finding book by author" + author.getName() + " " + author.getSurname(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Book> getByGenre(Genre genre) {
        try {
            return bookDao.findByGenre(genre);
        } catch (RuntimeException ex) {
            log.error("Error with finding book by genre {}, {}" + genre.getType(), ex.getCause());
            throw new DaoException("Error with finding book by genre" + genre.getType(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Book> getByTitleAndAuthor(Book book) {
        try {
            return bookDao.findByTitleAndAuthor(book);
        } catch (RuntimeException ex) {
            log.error("Error with finding book by by author and title {}, {}" + book.getTitle(), ex.getCause());
            throw new DaoException("Error with finding book by author and title" + book.getTitle(), ex);
        }

    }

}
