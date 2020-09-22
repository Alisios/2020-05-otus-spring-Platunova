package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "authors")
@EqualsAndHashCode(of = "id")
public class Author {

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("surname")
    private String surname;

    @Field("books")
   List<String> listOfBookTitles = new ArrayList<>();

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Author(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Author(String name, String surname, String ... listOfBookTitles) {
        this.name = name;
        this.surname = surname;
        this.listOfBookTitles.addAll(List.of(listOfBookTitles));
    }

    @Override
    public String toString() {
        return name +
                " " + surname +
                " " + listOfBookTitles +
                " (" + "id " + id + ")"
                ;
    }
}
