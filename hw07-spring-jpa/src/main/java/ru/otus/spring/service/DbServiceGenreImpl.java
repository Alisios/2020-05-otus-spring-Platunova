package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.DbException;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceGenreImpl implements DbServiceGenre {

    private final GenreRepository genreRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Genre save(Genre genre) {
        try {
            Optional<Genre> g = genreRepository.findByType(genre.getType());
            return g.orElseGet(() -> genreRepository.save(genre));
        } catch (RuntimeException ex) {
            throw new DbException("Error with saving genre " + genre.toString(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        try {
            genreRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DbException("Error with deleting genre with id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Genre> getAll() {
        try {
            return genreRepository.findAll();
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding all genres", ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Genre> getById(long id) {
        try {
            return genreRepository.findById(id);
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding genre by id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<Genre> getByType(String type) {
        try {
            return genreRepository.findByType(type);
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding genre by type " + type, ex);
        }
    }
}
