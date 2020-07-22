package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.DbException;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    @Transactional
    public Genre create(Genre genre) {
        try {
            Optional<Genre> g = genreDao.findByType(genre.getType());
            return g.orElseGet(() -> genreDao.insert(genre));
        } catch (Exception ex) {
            throw new DbException("Error with inserting genre " + genre.toString(), ex);
        }
    }

    @Override
    @Transactional
    public void save(Genre genre) {
        try {
            genreDao.update(genre);
        } catch (Exception ex) {
            throw new DbException("Error with updating genre " + genre.toString(), ex);
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            genreDao.deleteById(id);
        } catch (Exception ex) {
            throw new DbException("Error with deleting genre with id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        try {
            return genreDao.findAll();
        } catch (Exception ex) {
            throw new DbException("Error with finding all genres", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> getById(long id) {
        try {
            return genreDao.findById(id);
        } catch (Exception ex) {
            throw new DbException("Error with finding genre by id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> getByType(String type) {
        try {
            return genreDao.findByType(type);
        } catch (Exception ex) {
            throw new DbException("Error with finding genre by type " + type, ex);
        }
    }
}
