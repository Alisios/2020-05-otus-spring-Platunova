package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
@EqualsAndHashCode(of = "id")
public class Book {

    @Id
    private String id;

    @NotNull
    @Field("title")
    private String title;

    @NotNull
    @Field("author")
    private Author author;

    @Field("genre")
    private Genre genre;

    public Book(String title) {
        this.title = title;
    }

    public Book(String id, String title) {
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
                "\", Автор: " + author.getName() + " " + author.getSurname() +
                ", Жанр: " + genre.getName();
    }
}
