package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Author save(Author author) {
        try {
            Optional<Author> b = authorRepository.findFirstByNameAndSurname(author.getName(), author.getSurname());
            if (b.isEmpty())
                return authorRepository.save(author);
            else if (!b.get().getListOfBookTitles().contains(author.getListOfBookTitles().get(0))) {
                b.get().getListOfBookTitles().add(author.getListOfBookTitles().get(0));
                return authorRepository.updateBookTitleList(b.get());
            } else
                return b.get();
        } catch (Exception ex) {
            throw new DbException("Error with saving author " + author.toString(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByNameAndSurname(String name, String surname) {
        try {
            Author author = authorRepository.deleteAuthorByNameAndAndSurname(name, surname);
            bookRepository.deleteBooksByAuthor(name, surname);
        } catch (Exception ex) {
            throw new DbException("Error with deleting author  " + name + " " + surname, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Author> getAll() {
        try {
            return authorRepository.findAll();
        } catch (Exception ex) {
            throw new DbException("Error with finding all authors", ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<String> getBookTitlesByAuthor(String name, String surname) {
        try {
            Optional<Author> b = authorRepository.findFirstByNameAndSurname(name, surname);
            return b.orElseThrow().getListOfBookTitles();
        } catch (Exception ex) {
            throw new DbException("Impossible to get books. Error with finding author by name " + name + " " + surname, ex);
        }
    }
}
