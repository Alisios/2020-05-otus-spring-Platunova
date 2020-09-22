package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.Date;
import java.util.List;

public interface CommentService {

    Comment save(Comment genre);

    List<Comment> getAll();

    List<Comment> findByText(String text);

    List<Comment> findByBook(Book book);

    void deleteByBook(String title, String name, String surname);

    void deleteByBookAndText(Book book, String text);

    List<Comment> findCommentsBeforeDate(Date date);

    List<Comment> findCommentsAfterDate(Date date);
}
