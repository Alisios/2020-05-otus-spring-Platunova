package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.DaoException;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceAuthorImpl implements DbServiceAuthor {

    private final AuthorDao authorDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Author create(Author author) {
        try {
            Optional<Author> a = authorDao.findByFullName(author.getName(), author.getSurname());
            return a.orElseGet(() -> authorDao.insert(author));
        } catch (RuntimeException ex) {
            throw new DaoException("Error with inserting author " + author.toString(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Author author) {
        try {
            authorDao.update(author);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with updating author " + author.toString(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        try {
            authorDao.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with deleting author with id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Author> getAll() {
        try {
            return authorDao.findAll();
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding all authors", ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Author> getById(long id) {
        try {
            return authorDao.findById(id);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding author by id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Author> getByName(String name, String surname) {
        try {
            return authorDao.findByFullName(name, surname);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding author by name " + name + " " + surname, ex);
        }
    }
}
