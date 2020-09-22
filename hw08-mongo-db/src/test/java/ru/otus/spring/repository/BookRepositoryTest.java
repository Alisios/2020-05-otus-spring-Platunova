package ru.otus.spring.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.RepositoryTestAnnotation;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тесты проверяют, что репозиторий книг:")
@RepositoryTestAnnotation
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("корректно создает новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyInsertNewBook() {
        Book book = new Book("Норвежский Лес");
        Author author = new Author("Харуки", "Мураками");
        book.setGenre(new Genre("роман"));
        book.setAuthor(author);
        assertThat(bookRepository.save(book))
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("genre", book.getGenre());
    }


    @Test
    @DisplayName("корректно обновляет книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyUpdateBook() {
        var book = new Book("Норвежский Лес");
        var author = new Author("Харуки", "Мураками");
        book.setGenre(new Genre("роман"));
        book.setAuthor(author);
        var bookNew = bookRepository.save(book);
        bookNew.setTitle("Вино из топора");
        bookRepository.save(bookNew);
        assertThat(bookRepository.findById(bookNew.getId()).get())
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", bookNew.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("genre", bookNew.getGenre());
    }

    @Test
    @DisplayName("корректно удаляет книгу по названию и автору, в том числе из списка книг")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteBookByTitleAndAuthor() {
        long count1 = bookRepository.count();
        var book = bookRepository.findFirstByTitleAndAuthor_NameAndAuthor_Surname("Гарри Поттер и тайная комната", "Джоан", "Роулинг").get();
        bookRepository.deleteBookByTitleAndAuthor(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname());
        assertThat(bookRepository.count()).isEqualTo(count1 - 1);
        assertThat(authorRepository.findFirstByNameAndSurname("Джоан", "Роулинг").get().getListOfBookTitles()).doesNotContain(book.getTitle());
        assertThat(bookRepository.findAll()).doesNotContain(book);
        assertThrows(RuntimeException.class, () -> bookRepository.deleteBookByTitleAndAuthor("sdg", "sfgs", "sgsf"));
    }

    @Test
    @DisplayName("корректно удаляет все книги этого автора")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteBooksByAuthor() {
        long count1 = bookRepository.count();
        var author = authorRepository.findFirstByNameAndSurname("Джоан", "Роулинг").get();
        long size = author.getListOfBookTitles().size();
        bookRepository.deleteBooksByAuthor(author.getName(), author.getSurname());
        assertThat(bookRepository.count()).isEqualTo(count1 - size);
    }

    @DisplayName("загружает список всех книг с полной информацией о них (без комментариев)")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyReturnCorrectBooksListWithAllInfo() {
        val books = bookRepository.findAll();
        assertThat(books).isNotNull().hasSizeGreaterThan(4)
                .allMatch(s -> !s.getTitle().equals(""))
                .allMatch(s -> s.getAuthor() != null)
                .allMatch(s -> s.getGenre() != null);
    }

    @Test
    @DisplayName("корректно находит книгу по назанию и автору и не кидает исключение при их отсуствии")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindBookByTitleAndAuthor() {
        Book book = new Book("Гарри Поттер и тайная комната");
        Genre genre = new Genre("фэнтези");
        Author author = new Author("Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        Book bookn = bookRepository.findFirstByTitleAndAuthor_NameAndAuthor_Surname(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname()).get();
        assertThat(bookn)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrProperty("author")
                .hasFieldOrProperty("genre");

        assertAll("bookn", () -> {
            assertThat(bookn.getAuthor().getName()).isEqualTo(author.getName());
            assertThat(bookn.getAuthor().getSurname()).isEqualTo(author.getSurname());
            assertThat(bookn.getGenre().getName()).isEqualTo(genre.getName());
        });
    }

    @Test
    @DisplayName("не находит книгу по автору и названию при несовпадении автора")
    void doesNotFindBookByTitleAndAuthorWithWrongAuthor() {
        assertThat(bookRepository.findFirstByTitleAndAuthor_NameAndAuthor_Surname("Гарри Поттер и Философский камень", "Харуки", "Мураками")).isEmpty();
    }

    @Test
    @DisplayName("не находит книгу по автору и названию при несовпадении наименования")
    void doesNotFindBookByTitleAndAuthorWithWrongTitle() {
        assertThat(bookRepository.findFirstByTitleAndAuthor_NameAndAuthor_Surname("Гарри Поттер и Дары смерти", "Джоан", "Роулинг")).isEmpty();
    }


    @Test
    @DisplayName("корректно находит книги по автору")
    void correctlyFindBookByAuthor() {
        assertThat(bookRepository.findFirstByAuthor_NameAndAuthor_Surname("Стивен", "Кинг")).isNotEmpty();
        assertThat(bookRepository.findFirstByAuthor_NameAndAuthor_Surname("Vb[fbk", "Пехов").isEmpty());
    }
}