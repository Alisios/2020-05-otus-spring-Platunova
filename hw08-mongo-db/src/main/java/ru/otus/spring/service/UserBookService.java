package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

public interface UserBookService {

    Book addBookByUser();

    Author addAuthorByUser();

    Author addAuthorWithTitleByUser(String title);

    Comment addCommentByUser();

    String printBooksWithComments();
}
