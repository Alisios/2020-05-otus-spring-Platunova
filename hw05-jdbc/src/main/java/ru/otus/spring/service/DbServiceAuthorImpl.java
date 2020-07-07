package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.DaoJdbcException;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceAuthorImpl implements DbServiceAuthor {

    private final AuthorDao authorDao;

    @Override
    public Author create(Author author) {
        try {
            Optional<Author> a = authorDao.findByFullName(author.getName(), author.getSurname());
            return a.orElseGet(() -> authorDao.insert(author));
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with inserting author " + author.toString(), ex);
        }
    }

    @Override
    public void save(Author author) {
        try {
            authorDao.update(author);
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with updating author " + author.toString(), ex);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            authorDao.deleteById(id);
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with deleting author with id " + id, ex);
        }
    }

    @Override
    public List<Author> getAll() {
        try {
            return authorDao.findAll();
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with finding all authors", ex);
        }
    }

    @Override
    public Optional<Author> getById(long id) {
        try {
            return authorDao.findById(id);
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with finding author by id " + id, ex);
        }
    }

    @Override
    public Optional<Author> getByName(String name, String surname) {
        try {
            return authorDao.findByFullName(name, surname);
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with finding author by name " + name + " " + surname, ex);
        }
    }
}
