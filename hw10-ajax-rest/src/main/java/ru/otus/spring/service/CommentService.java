package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentService {

    Comment save(Comment genre);

    void deleteById(long id);

    void deleteByBookId(long id);

    List<Comment> getAll();

    List<Comment> findByText(String text);

    List<Comment> findByBook(Book book);

    List<Comment> findByBookId(long id);
}
