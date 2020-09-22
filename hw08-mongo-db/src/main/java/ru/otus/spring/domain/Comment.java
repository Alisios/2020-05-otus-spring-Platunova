package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
@EqualsAndHashCode(of = "id")
public class Comment {

    @Id
    String id;

    @Field("text")
    String text;

    @Field("date")
    Date date;

    @NotNull
    @Field("book")
    Book book;

    public Comment(Book book, String text) {
        this.book = book;
        this.text = text;
    }

    public Comment(Book book, String text, Date date) {
        this.book = book;
        this.text = text;
        this.date = date;
    }

    public Comment(String id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return"Комментарий ("+ date + "): \"" + text + " к книге:\n " + book;
    }

    public String toStringWithoutBook() {
        return "Комментарий ("+ date + "): \"" + text + "\" (id " + id + ")";
    }
}
