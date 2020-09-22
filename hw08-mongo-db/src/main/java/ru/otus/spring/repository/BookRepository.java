package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(String id);

    List<Book> findAllByTitle(String title);

    List<Book> findAllByGenre_Name(String genre);

    List<Book> findFirstByAuthor_NameAndAuthor_Surname(String name, String surname);

    Optional<Book> findFirstByTitleAndAuthor_NameAndAuthor_Surname(String title, String name, String surname);

}
