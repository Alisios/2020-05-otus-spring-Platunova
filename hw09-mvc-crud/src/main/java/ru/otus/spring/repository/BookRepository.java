package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book save(Book book);

    void deleteById(long id);

    @EntityGraph(value = "book-author-genre-entity-graph")
    List<Book> findAll();

    Optional<Book> findById(long id);

    @EntityGraph(value = "book-author-genre-entity-graph")
    @Query("FROM Book u WHERE u.genre.type=:genre")
    List<Book> findByGenre(String genre);

    @EntityGraph(value = "book-author-genre-entity-graph")
    @Query("FROM Book u WHERE u.author.name=:name and u.author.surname =:surname")
    List<Book> findByAuthor(String name, String surname);

    @EntityGraph(value = "book-author-genre-entity-graph")
    @Query("FROM Book u WHERE u.title=:title and u.author.name=:name and u.author.surname =:surname")
    Optional<Book> findByTitleAndAuthor(String title, String name, String surname);

    @EntityGraph(value = "book-author-genre-entity-graph")
    @Query("FROM Book u WHERE u.title=:title and u.author.name=:name and u.author.surname =:surname and u.genre.type=:type")
    Optional<Book> findByTitleAndAuthorAndGenre_Type(String title, String name, String surname, String type);

    long count();

}
