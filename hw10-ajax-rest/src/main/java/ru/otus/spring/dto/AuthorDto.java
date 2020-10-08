package ru.otus.spring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorDto implements Serializable {

    private static final long serialVersionUID = 129348782L;

    private long id;

    private String name;

    private String surname;

    private List<String> titleList = new ArrayList<>();

    public static AuthorDto toAuthorDto(Author author ) {
        return new AuthorDto(author.getId(), author.getName(), author.getSurname(), author.getListOfBook().stream().map(Book::getTitle).collect(Collectors.toList()));
    }

    public static Author fromAuthorDto(AuthorDto author) {
        return new Author(author.getName(), author.getSurname());
    }

    @Override
    public String toString() {
        return name +
                " " + surname + " (" +
                "id " + id +
                ")";
    }

}
