package ru.otus.spring;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.DbException;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.BookServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("Интеграционный тест проверяет:")
@EnableJpaRepositories(basePackages = "ru.otus.spring.repository")
@EntityScan("ru.otus.spring.domain")
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class IntegrationBookServiceTest {

    @Configuration
    @Import({BookServiceImpl.class})
    static class NestedConfiguration {}

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Test
    @DisplayName("корректно создает новую книгу при отсуствии ее в таблице")
    void correctlySaveBookIfDoesNotExist() {
        val book = new Book("Норвежский Лес");
        val author = new Author("Харуки", "Мураками");
        book.setAuthor(author);
        val genre = new Genre("роман");
        book.setGenre(genre);
        long count = bookRepository.count();
        assertThat(bookService.save(book))
                .hasFieldOrPropertyWithValue("title", "Норвежский Лес")
                .hasFieldOrPropertyWithValue("author", author)
                .hasFieldOrPropertyWithValue("genre", genre);
        assertThat(bookRepository.count()).isEqualTo(count + 1);
    }

    @Test
    @DisplayName("корректно создает новую книгу при отсуствии ее в таблице")
    void rollbackActionIfExceptionWasThrown() {
        val book = new Book("Норвежский Лес");
        val author = new Author("Харуки", "Мураками");
        book.setAuthor(author);
        long count = bookRepository.count();
        book.setGenre(null);
        assertThatThrownBy(()->bookService.save(book))
                .isInstanceOf(DbException.class)
                .hasMessageContaining("with saving book with");
        assertThat(bookRepository.count()).isEqualTo(count);
    }

}
