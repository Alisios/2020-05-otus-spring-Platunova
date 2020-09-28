package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

public interface UserIOService {

    Book getBookInfoFromUser();

    Author getAuthorInfoFromUser();
}
