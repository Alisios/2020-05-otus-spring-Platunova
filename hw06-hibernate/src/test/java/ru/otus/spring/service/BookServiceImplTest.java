package ru.otus.spring.service;

import lombok.val;
import ru.otus.spring.dao.DaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют, что сервис книг:")
class DbServiceBookImplTest {

    @Configuration
    @Import(BookServiceImpl.class)
    static class NestedConfiguration {
    }

    @MockBean
    private BookDao bookDao;

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("корректно создает новую книгу при отсуствии ее в таблице")
    void correctlySaveBookIfDoesNotExist() {
        val book = new Book("Норвежский Лес");
        val author = new Author("Харуки", "Мураками");
        book.setAuthor(author);
        val book2 = new Book(1L, "Норвежский Лес", author, null);
        when(bookDao.findByTitleAndAuthor(book)).thenReturn(Optional.empty());
        when(bookDao.insert(book)).thenReturn(book2);
        assertThat(bookService.create(book)).isEqualTo(book2);
        verify(bookDao, times(1)).insert(book);
    }

    @Test
    @DisplayName("корректно создает новой книги при наличии ее в таблице")
    void correctlySaveBookIfExists() {
        val book = new Book("Норвежский Лес");
        val author = new Author("Харуки", "Мураками");
        book.setAuthor(author);
        val book2 = new Book(1L, "Норвежский Лес", author, null);
        when(bookDao.findByTitleAndAuthor(book)).thenReturn(Optional.of(book2));
        assertThat(bookService.create(book)).isEqualTo(book2);
        verify(bookDao, times(0)).insert(book);
    }

    @Test
    @DisplayName("корректно обрабатывает исключение дб")
    void correctlyHandleDBException() {
        val author = new Author(1, "Харуки", "Мураками");
        val book = new Book(1L, "Норвежский Лес", author, null);
        doThrow(RuntimeException.class)
                .when(bookDao).findByTitleAndAuthor(any());
        Throwable thrown = assertThrows(DaoException.class, () -> bookService.create(book));
        assertThat(thrown).hasMessageContaining("Error with inserting book").hasMessageContaining("Норвежский Лес");
    }
}