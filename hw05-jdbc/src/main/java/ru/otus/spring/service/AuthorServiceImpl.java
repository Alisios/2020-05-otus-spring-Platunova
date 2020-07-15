package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.DaoException;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Transactional (rollbackFor = Exception.class)
    @Override
    public Author create(Author author) {
        try {
            Optional<Author> a = authorDao.findByFullName(author.getName(), author.getSurname());
            return a.orElseGet(() -> authorDao.insert(author));
        } catch (DataAccessException ex) {
            throw new DaoException("Error with inserting author " + author.toString(), ex);
        }
    }

    @Transactional (rollbackFor = Exception.class)
    @Override
    public void save(Author author) {
        try {
            authorDao.update(author);
        } catch (DataAccessException ex) {
            throw new DaoException("Error with updating author " + author.toString(), ex);
        }
    }

    @Transactional (rollbackFor = Exception.class)
    @Override
    public void deleteById(long id) {
        try {
            authorDao.deleteById(id);
        } catch (DataAccessException ex) {
            throw new DaoException("Error with deleting author with id " + id, ex);
        }
    }

    @Transactional (readOnly = true, rollbackFor = Exception.class)
    @Override
    public List<Author> getAll() {
        try {
            return authorDao.findAll();
        } catch (DataAccessException ex) {
            throw new DaoException("Error with finding all authors", ex);
        }
    }

    @Override
    @Transactional (readOnly = true, rollbackFor = Exception.class)
    public Optional<Author> getById(long id) {
        try {
            return authorDao.findById(id);
        } catch (DataAccessException ex) {
            throw new DaoException("Error with finding author by id " + id, ex);
        }
    }

    @Override
    @Transactional (readOnly = true, rollbackFor = Exception.class)
    public Optional<Author> getByName(String name, String surname) {
        try {
            return authorDao.findByFullName(name, surname);
        } catch (DataAccessException ex) {
            throw new DaoException("Error with finding author by name " + name + " " + surname, ex);
        }
    }
}
