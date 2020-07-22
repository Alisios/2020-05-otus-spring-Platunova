package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.DbException;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    @Transactional
    public Author create(Author author) {
        try {
            Optional<Author> a = authorDao.findByFullName(author.getName(), author.getSurname());
            return a.orElseGet(() -> authorDao.insert(author));
        } catch (Exception ex) {
            throw new DbException("Error with inserting author " + author.toString(), ex);
        }
    }

    @Override
    @Transactional
    public void save(Author author) {
        try {
            authorDao.update(author);
        } catch (Exception ex) {
            throw new DbException("Error with updating author " + author.toString(), ex);
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            authorDao.deleteById(id);
        } catch (Exception ex) {
            throw new DbException("Error with deleting author with id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        try {
            return authorDao.findAll();
        } catch (Exception ex) {
            throw new DbException("Error with finding all authors", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> getById(long id) {
        try {
            return authorDao.findById(id);
        } catch (Exception ex) {
            throw new DbException("Error with finding author by id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> getByName(String name, String surname) {
        try {
            return authorDao.findByFullName(name, surname);
        } catch (Exception ex) {
            throw new DbException("Error with finding author by name " + name + " " + surname, ex);
        }
    }
}
