package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.spring.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    Comment save(Comment comment);

    void deleteById(long id);


    @EntityGraph(value = "book-comment-entity-graph")
    void deleteByBookId(long id);

    @EntityGraph(value = "book-comment-entity-graph")
    List<Comment> findAll();

    Optional<Comment> findById(long id);

    @EntityGraph(value = "book-comment-entity-graph")
    List<Comment> findByTextContainingIgnoreCase(String text);

    @EntityGraph(value = "book-comment-entity-graph")
    @Query("FROM Comment u WHERE u.book.title=:title and u.book.author.name=:name and u.book.author.surname =:surname")
    List<Comment> findByBook(String title, String name, String surname);

    @EntityGraph(value = "book-comment-entity-graph")
    List<Comment> findByBookId(long id);

    long count();
}
