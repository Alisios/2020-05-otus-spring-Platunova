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
    @Import(UserBookServiceImpl.class)
    static class NestedConfiguration {
    }

    @Autowired
    private UserBookService userBookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("корректно обрабатывает сообщение пользователя и добавляет книгу")
    void correctlyHandleUserMessageAndAddNewBook() {
        Book book = new Book("Гарри Поттер и Философский камень");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        when(genreService.save(any())).thenReturn(genre);
        when(authorService.save(any())).thenReturn(author);
        book.setAuthor(author);
        book.setGenre(genre);
        when(bookService.save(book)).thenReturn(book);
        assertThat(userBookService.addBookByUser(book)).isEqualTo(book);
    }

}