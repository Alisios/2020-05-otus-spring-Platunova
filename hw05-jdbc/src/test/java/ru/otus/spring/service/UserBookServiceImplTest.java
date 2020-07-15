package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Тесты проверяют:")
class UserBookServiceImplTest {


    @Configuration
    @Import({UserBookServiceImpl.class})
    static class NestedConfiguration {
    }

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService dbServiceAuthor;

    @MockBean
    private BookService bookService;

    @MockBean
    private IOService ioService;

    @Autowired
    private UserBookService userBookService;

    @Test
    @DisplayName("корректно обрабатывает сообщение пользователя и добавляет книгу")
    void correctlyHandleUserMessageAndAddNewBook() {
        Book book = new Book("Гарри Поттер и Философский камень");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        when(ioService.inputMessage()).thenReturn("Гарри Поттер и Философский камень").thenReturn(author.getName()).thenReturn(author.getSurname()).thenReturn(genre.getType());
        when(genreService.create(any())).thenReturn(genre);
        when(dbServiceAuthor.create(any())).thenReturn(author);
        book.setAuthor(author);
        book.setGenre(genre);
        when(bookService.create(book)).thenReturn(book);
        assertThat(userBookService.addBookByUser()).isEqualTo(book);
    }

    @Test
    @DisplayName("корректно обрабатывает сообщение пользователя и добавляет жанр")
    void correctlyHandleUserMessageAndAddNewGenre() {
        Genre genre = new Genre("фэнтези");
        when(ioService.inputMessage()).thenReturn(genre.getType());
        when(genreService.create(genre)).thenReturn(genre);
        assertThat(userBookService.addGenreByUser()).isEqualTo(genre);
    }

    @Test
    @DisplayName("корректно обрабатывает сообщение пользователя и добавляет автора")
    void correctlyHandleUserMessageAndAddNewAuthor() {
        Author author = new Author("Рэй", "Брэдбери");
        when(ioService.inputMessage()).thenReturn(author.getName()).thenReturn(author.getSurname());
        when(dbServiceAuthor.create(author)).thenReturn(author);
        assertThat(userBookService.addAuthorByUser()).isEqualTo(author);
    }
}