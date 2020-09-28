package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {

    Optional<Author> findFirstByNameAndSurname(String name, String surname);

    Author deleteAuthorByNameAndAndSurname(String name, String surname);

}
