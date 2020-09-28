package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.ServiceException;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public Genre save(Genre genre) {
        try {
            Optional<Genre> g = genreRepository.findByType(genre.getType());
            return g.orElseGet(() -> genreRepository.save(genre));
        } catch (Exception ex) {
            throw new ServiceException("Error with saving genre " + genre.toString(), ex);
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            genreRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ServiceException("Error with deleting genre with id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        try {
            return genreRepository.findAll();
        } catch (Exception ex) {
            throw new ServiceException("Error with finding all genres", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> getById(long id) {
        try {
            return genreRepository.findById(id);
        } catch (Exception ex) {
            throw new ServiceException("Error with finding genre by id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> getByType(String type) {
        try {
            return genreRepository.findByType(type);
        } catch (Exception ex) {
            throw new ServiceException("Error with finding genre by type " + type, ex);
        }
    }
}
