package ru.otus.spring.service;

import ru.otus.spring.domain.Book;

public interface UserBookService {

    Book addBookByUser(Book book);
}