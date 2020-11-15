package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.ServiceException;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public Author save(Author author) {
        try {
           // Optional<Author> b = authorRepository.findByNameAndSurname(author.getName(), author.getSurname());
            return authorRepository.save(author);
           // return b.orElseGet(() -> authorRepository.save(author));
        } catch (Exception ex) {
            throw new ServiceException("Error with saving author " + author.toString(), ex);
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            authorRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ServiceException("Error with deleting author with id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        try {
            return authorRepository.findAll();
        } catch (Exception ex) {
            throw new ServiceException("Error with finding all authors", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> getById(long id) {
        try {
            return authorRepository.findById(id);
        } catch (Exception ex) {
            throw new ServiceException("Error with finding author by id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> getByName(String name, String surname) {
        try {
            return authorRepository.findByNameAndSurname(name, surname);
        } catch (Exception ex) {
            throw new ServiceException("Error with finding author by name " + name + " " + surname, ex);
        }
    }
}
