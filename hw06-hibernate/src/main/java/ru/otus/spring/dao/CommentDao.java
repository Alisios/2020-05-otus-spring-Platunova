package ru.otus.spring.dao;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDao {

    Comment insert(Comment comment);

    void update(Comment comment);

    void deleteById(long id);

    void deleteByBookId(long id);

    List<Comment> findAll();

    Optional<Comment> findById(long id);

    List<Comment> findByText(String text);

    List<Comment> findByBook(Book book);

    List<Comment> findByBookId(long id);

    long count();
}
