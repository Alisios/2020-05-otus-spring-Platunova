package ru.otus.spring.repository;

public interface CommentRepositoryCustom {

    void deleteAllByBook(String title, String name, String surname);
}
