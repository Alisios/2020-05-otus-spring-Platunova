package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre {

    private long id;

    private String type;

    public Genre(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type + " (id " + id + ')';
    }
}
