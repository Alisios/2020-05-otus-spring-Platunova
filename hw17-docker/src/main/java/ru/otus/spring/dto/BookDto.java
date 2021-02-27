package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.spring.dto.AuthorDto.fromAuthorDto;
import static ru.otus.spring.dto.AuthorDto.toAuthorDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class BookDto implements Serializable {
    private static final long serialVersionUID = 171348787L;
    private long id;

    private String title;

    private AuthorDto authorDto;

    private Genre genre;

    private List<CommentDto> commentsDto = new ArrayList<>();

    public static BookDto toBookDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), toAuthorDto(book.getAuthor()), book.getGenre(), book.getComments().stream().map(CommentDto::toCommentDto).collect(Collectors.toList()));
    }

    public static Book fromBookDto(BookDto bookDto) {
        return new Book(bookDto.getId(), bookDto.getTitle(), fromAuthorDto(bookDto.getAuthorDto()), bookDto.getGenre());
    }

    @Override
    public String toString() {
        return "Название книги (id " + id + "): \""
                + title +
                "\", Автор: " + authorDto +
                ", Жанр: " + genre;
    }
}
