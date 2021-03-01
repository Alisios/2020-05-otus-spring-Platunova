package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Author {

    private long id;

    private String name;

    private String surname;

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }


    @Override
    public String toString() {
        return name +
                " " + surname + " (" +
                "id " + id +
                ")";
    }
}
