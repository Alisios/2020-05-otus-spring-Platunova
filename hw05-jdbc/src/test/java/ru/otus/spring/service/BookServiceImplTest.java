package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.PermissionDeniedDataAccessException;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.DaoException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют:")
class BookServiceImplTest {

    @Configuration("ru.otus.spring.service")
    static class NestedConfiguration {
        @MockBean
        private BookDao bookDao;

        @Bean
        BookService dbServiceBook() {
            return new BookServiceImpl(bookDao);
        }
    }

    @Autowired
    private BookService bookService;

    @Autowired
    private BookDao bookDao;

    @Test
    @DisplayName("корректно обрабатывает исключение дб")
    void correctlyHandleDBException() {
        Book book = new Book("Сказка");
        doThrow(PermissionDeniedDataAccessException.class).when(bookDao).insert(book);
        Throwable thrown = assertThrows(DaoException.class, () -> {
            bookService.create(book);
        });
        assertThat(thrown).hasMessageContaining("Error with inserting").hasMessageContaining("Сказка");
    }

    @Test
    @DisplayName("корректно создает новую книгу при ее отсуствии")
    void correctlyCreateBookIfDoesNotExist() {
        Book book = new Book(1, "Сказка");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        when(bookDao.findByTitleAndAuthor(book)).thenReturn(Optional.empty());
        when(bookDao.insert(book)).thenReturn((book));
        assertThat(bookService.create(book))
                .hasNoNullFieldsOrProperties()
                .isEqualTo(book);
        verify(bookDao, times(1)).insert(book);
    }

    @Test
    @DisplayName("корректно не создает новую книгу при ее наличии")
    void correctlyDoesNotCreateBookIfExists() {
        Book book = new Book(1, "Сказка");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        when(bookDao.findByTitleAndAuthor(book)).thenReturn(Optional.of(book));
        assertThat(bookService.create(book))
                .hasNoNullFieldsOrProperties()
                .isEqualTo(book);
        verify(bookDao, times(0)).insert(book);
    }

    @Test
    @DisplayName("корректно обрабатывает запрос удаления")
    void correctlyDeleteBook() {
        bookService.deleteById(1);
        verify(bookDao, times(1)).deleteById(1);
        doThrow(PermissionDeniedDataAccessException.class).when(bookDao).deleteById(1);
        Throwable thrown = assertThrows(DaoException.class, () -> {
            bookService.deleteById(1);
        });
        assertThat(thrown).hasMessageContaining("Error with deleting book").hasMessageContaining("1");
    }
}