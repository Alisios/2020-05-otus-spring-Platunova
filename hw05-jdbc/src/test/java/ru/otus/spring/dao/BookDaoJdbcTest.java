package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют:")
@JdbcTest
@Import(BookDaoJdbc.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookDaoJdbcTest {

    @Autowired
    private BookDaoJdbc bookDaoJdbc;

    @Test
    @DisplayName("корректно создает новую книгу")
    void correctlyInsertNewBook() {
        Book book = new Book("Норвежский Лес");
        Genre genre = new Genre(3, "ужасы");
        Author author = new Author(4, "Харуки", "Мураками");
        book.setGenre(genre);
        book.setAuthor(author);
        assertThat(bookDaoJdbc.insert(book).get())
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("genre", book.getGenre());
    }

    @Test
    @DisplayName("корректно вставляет ту же самую книгу")
    void correctlyInsertTheSameBook() {
        Book book = new Book("Норвежский Лес");
        Genre genre = new Genre(3, "ужасы");
        Author author = new Author(4, "Харуки", "Мураками");
        book.setGenre(genre);
        book.setAuthor(author);
        int count1 = bookDaoJdbc.count();
        bookDaoJdbc.insert(book);
        assertThat(bookDaoJdbc.count()).isEqualTo(count1 + 1);
        bookDaoJdbc.insert(book);
        assertThat(bookDaoJdbc.count()).isEqualTo(count1 + 2);
    }

    @Test
    @DisplayName("корректно обновляет книгу")
    void correctlyUpdateBook() {
        Book book = new Book("Норвежский Лес");
        Genre genre = new Genre(3, "ужасы");
        Author author = new Author(4, "Харуки", "Мураками");
        book.setGenre(genre);
        book.setAuthor(author);
        Book bookNew = bookDaoJdbc.insert(book).get();
        bookNew.setTitle("Вино из топора");
        bookDaoJdbc.updateByTitle(bookNew);
        assertThat(bookDaoJdbc.findById(bookNew.getId()).get())
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", bookNew.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("genre", bookNew.getGenre());
    }

    @Test
    @DisplayName("кидает исключение при нулевй книге")
    void correctlyThrowExceptions() {
        assertThrows(Exception.class, () -> {
            bookDaoJdbc.insert(null);
        });
        assertThrows(Exception.class, () -> {
            bookDaoJdbc.updateByTitle(null);
        });
    }

    @Test
    @DisplayName("корректно удаляет книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteBookById() {
        int count1 = bookDaoJdbc.count();
        Book book = bookDaoJdbc.findById(1L).get();
        bookDaoJdbc.deleteById(1L);
        assertThat(bookDaoJdbc.count()).isEqualTo(count1 - 1);
        assertThat(bookDaoJdbc.findAll()).doesNotContain(book);
        assertDoesNotThrow(() -> bookDaoJdbc.deleteById(321L));
    }

    @Test
    @DisplayName("корректно выдает все книги")
    void correctlyFindingAllBooks() {
        int count1 = bookDaoJdbc.count();
        System.out.println(bookDaoJdbc.findAll());
        assertThat(bookDaoJdbc.findAll().size()).isNotNull().isEqualTo(count1);
    }

    @Test
    @DisplayName("корректно находит книгу по id и не кидает исключение при отсуствии id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindBookById() {
        Book book = new Book("Гарри Поттер и Философский камень");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        Book bookn = bookDaoJdbc.findById(1L).get();
        assertThat(bookn)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrProperty("author");
        assertThat(bookn.getAuthor().getName()).isEqualTo(book.getAuthor().getName());
        assertThat(bookn.getGenre().getType()).isEqualTo(book.getGenre().getType());
        assertDoesNotThrow(() -> {
            assertThat(bookDaoJdbc.findById(312)).isEmpty();
        });
    }

    @Test
    @DisplayName("корректно находит книгу по назанию и автору и не кидает исключение при их отсуствии")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindBookByTitleAndAuthor() {
        Book book = new Book("Гарри Поттер и Философский камень");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        Book bookn = bookDaoJdbc.findByTitleAndAuthor(book).get();
        assertThat(bookn)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("genre", book.getGenre());
    }

    @Test
    @DisplayName("не находит книгу по автору и названию при несовпадении автора")
    void doesNotFindBookByTitleAndAuthorWithWrongAuthor() {
        Book book = new Book("Гарри Поттер и Философский камень");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Харуки", "Мураками");
        book.setGenre(genre);
        book.setAuthor(author);
        assertThat(bookDaoJdbc.findByTitleAndAuthor(book)).isEmpty();
    }

    @Test
    @DisplayName("не находит книгу по автору и названию при несовпадении наименования")
    void doesNotFindBookByTitleAndAuthorWithWrongTitle() {
        Book book = new Book("Гарри Поттер и Дары смерти");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        assertThat(bookDaoJdbc.findByTitleAndAuthor(book)).isEmpty();
    }

    @Test
    @DisplayName("корректно находит книги по жанру")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindBookByGenre() {
        List<Book> booksByG = bookDaoJdbc.findByGenre(new Genre(1, "фэнтези"));
        assertThat(booksByG).isNotEmpty().contains(bookDaoJdbc.findById(1).get());
        assertThat(bookDaoJdbc.findByGenre(new Genre("журнал")).isEmpty());
    }

    @Test
    @DisplayName("корректно находит книги по автору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindBookByAuthor() {
        List<Book> booksByG = bookDaoJdbc.findByAuthor(new Author(1, "Джоан", "Роулинг"));
        assertThat(booksByG).isNotEmpty().contains(bookDaoJdbc.findById(1).get());
        assertThat(bookDaoJdbc.findByAuthor(new Author("Харуки", "Мураками")).isEmpty());
    }
}