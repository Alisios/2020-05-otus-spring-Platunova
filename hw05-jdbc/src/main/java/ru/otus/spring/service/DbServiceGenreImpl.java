package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.DaoJdbcException;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceGenreImpl implements DbServiceGenre {

    private final GenreDao genreDao;

    @Override
    public Genre create(Genre genre) {
        try {
            Optional<Genre> g = genreDao.findByType(genre.getType());
            return g.orElseGet(() -> genreDao.insert(genre));
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with inserting genre " + genre.toString(), ex);
        }
    }

    @Override
    public void save(Genre genre) {
        try {
            genreDao.update(genre);
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with updating genre " + genre.toString(), ex);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            genreDao.deleteById(id);
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with deleting genre with id " + id, ex);
        }
    }

    @Override
    public List<Genre> getAll() {
        try {
            return genreDao.findAll();
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with finding all genres", ex);
        }
    }

    @Override
    public Optional<Genre> getById(long id) {
        try {
            return genreDao.findById(id);
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with finding genre by id " + id, ex);
        }
    }

    @Override
    public Optional<Genre> getByType(String type) {
        try {
            return genreDao.findByType(type);
        } catch (DataAccessException ex) {
            throw new DaoJdbcException("Error with finding genre by type " + type, ex);
        }
    }
}
