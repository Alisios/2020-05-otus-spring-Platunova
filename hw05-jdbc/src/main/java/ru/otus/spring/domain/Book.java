package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {

    private long id;

    private String title;

    private Author author;

    private Genre genre;

    public Book(String title) {
        this.title = title;
    }

    public Book(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Book(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Название книги (id " + id + "): \""
                + title +
                "\", Автор: " + author +
                ", Жанр: " + genre;
    }
}
