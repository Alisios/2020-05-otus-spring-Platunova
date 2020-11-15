package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author save(Author book);

    void deleteById(long id);

    List<Author> findAll();

    Optional<Author> findById(long id);

    Optional<Author> findByNameAndSurname(String name, String surname);

    long count();

}
