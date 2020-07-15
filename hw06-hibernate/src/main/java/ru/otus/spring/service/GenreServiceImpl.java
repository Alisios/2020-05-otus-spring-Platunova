package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.DaoException;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Genre create(Genre genre) {
        try {
            Optional<Genre> g = genreDao.findByType(genre.getType());
            return g.orElseGet(() -> genreDao.insert(genre));
        } catch (RuntimeException ex) {
            throw new DaoException("Error with inserting genre " + genre.toString(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Genre genre) {
        try {
            genreDao.update(genre);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with updating genre " + genre.toString(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        try {
            genreDao.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with deleting genre with id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Genre> getAll() {
        try {
            return genreDao.findAll();
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding all genres", ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Genre> getById(long id) {
        try {
            return genreDao.findById(id);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding genre by id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Genre> getByType(String type) {
        try {
            return genreDao.findByType(type);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding genre by type " + type, ex);
        }
    }
}
