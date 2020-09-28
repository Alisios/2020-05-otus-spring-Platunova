package ru.otus.spring.repository;

public interface BookRepositoryCustom {

    void deleteBookByTitleAndAuthor(String title, String name, String surname);

    void deleteBooksByAuthor(String name, String surname);

}
