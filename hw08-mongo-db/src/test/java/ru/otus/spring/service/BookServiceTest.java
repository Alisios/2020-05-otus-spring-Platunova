package ru.otus.spring.service;

import lombok.val;
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
import ru.otus.spring.repository.BookRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Тесты проверяют, что сервис книг:")
class BookServiceTest {

    @Configuration
    @Import(BookServiceImpl.class)
    static class NestedConfiguration {
    }

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("корректно создает новую книгу при отсуствии ее в таблице")
    void correctlySaveBookIfDoesNotExist() {
        val book = new Book("Норвежский Лес");
        val author = new Author("Харуки", "Мураками");
        book.setAuthor(author);
        val book2 = new Book("Норвежский Лес", author, new Genre("роман"));
        when(bookRepository.findFirstByTitleAndAuthor_NameAndAuthor_Surname(book.getTitle(), author.getName(), author.getSurname())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book2);
        assertThat(bookService.save(book)).isEqualTo(book2);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("корректно создает новой книги при наличии ее в таблице")
    void correctlySaveBookIfExists() {
        val book = new Book("Норвежский Лес");
        val author = new Author("Харуки", "Мураками");
        book.setAuthor(author);
        val book2 = new Book("Норвежский Лес", author, new Genre("роман"));
        when(bookRepository.findFirstByTitleAndAuthor_NameAndAuthor_Surname(book.getTitle(), author.getName(), author.getSurname())).thenReturn(Optional.of(book2));
        assertThat(bookService.save(book)).isEqualTo(book2);
        verify(bookRepository, times(0)).save(book);
    }

    @Test
    @DisplayName("корректно обрабатывает исключение дб")
    void correctlyHandleDBException() {
        val author = new Author("Харуки", "Мураками");
        val book = new Book("Норвежский Лес", author, new Genre("роман"));
        doThrow(RuntimeException.class)
                .when(bookRepository).findFirstByTitleAndAuthor_NameAndAuthor_Surname(anyString(), anyString(), anyString());
        Throwable thrown = assertThrows(DbException.class, () -> bookService.save(book));
        assertThat(thrown).hasMessageContaining("Error with saving book");
    }

}