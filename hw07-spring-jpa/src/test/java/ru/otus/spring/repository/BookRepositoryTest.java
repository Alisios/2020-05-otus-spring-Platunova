package ru.otus.spring.repository;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты проверяют, что репозиторий книги:")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("корректно создает новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyInsertNewBook() {
        Book book = new Book("Норвежский Лес");
        Author author = new Author("Харуки", "Мураками");
        em.persistAndFlush(author);
        book.setGenre(em.find(Genre.class, 3L));
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
        Book book = new Book("Норвежский Лес");
        Author author = new Author("Харуки", "Мураками");
        em.persistAndFlush(author);
        book.setGenre(em.find(Genre.class, 3L));
        book.setAuthor(author);
        Book bookNew = bookRepository.save(book);
        em.detach(bookNew);
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
    @DisplayName("кидает исключение при нулевй книге")
    void correctlyThrowExceptions() {
        assertThrows(DataAccessException.class, () -> bookRepository.save(null));
        assertThrows(DataAccessException.class, () -> bookRepository.save(null));
    }

    @Test
    @DisplayName("корректно удаляет книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteBookById() {
        long count1 = bookRepository.count();
        Book book = bookRepository.findById(1L).get();
        bookRepository.deleteById(1L);
        assertThat(bookRepository.count()).isEqualTo(count1 - 1);
        assertThat(bookRepository.findAll()).doesNotContain(book);
        assertThrows(DataAccessException.class, () -> bookRepository.deleteById(321L));
    }

    @DisplayName("загружает список всех книг с полной информацией о них (без комментариев)")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyReturnCorrectBooksListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        val books = bookRepository.findAll();
        assertThat(books).isNotNull().hasSize(4)
                .allMatch(s -> !s.getTitle().equals(""))
                .allMatch(s -> s.getAuthor() != null)
                .allMatch(s -> s.getGenre() != null);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @DisplayName("загружает список всех книг с полной информацией о них (с комментариями)")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyReturnCorrectBooksListWithComments() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        val books = bookRepository.findAll();
        assertThat(books).isNotNull().hasSize(4)
                .allMatch(s -> !s.getTitle().equals(""))
                .allMatch(s -> s.getAuthor() != null)
                .allMatch(s -> s.getGenre() != null)
                .allMatch(s -> s.getComments().size() > 0);
       assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("корректно находит книгу по id и не кидает исключение при отсуствии id")
    void correctlyFindBookById() {
        Book book = new Book("Гарри Поттер и Философский камень");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        Book bookn = bookRepository.findById(1L).get();
        assertThat(bookn)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrProperty("author");
        assertThat(bookn.getAuthor().getName()).isEqualTo(book.getAuthor().getName());
        assertThat(bookn.getGenre().getType()).isEqualTo(book.getGenre().getType());
        assertDoesNotThrow(() -> assertThat(bookRepository.findById(312)).isEmpty());
    }

    @Test
    @DisplayName("корректно находит книгу по назанию и автору и не кидает исключение при их отсуствии")
    void correctlyFindBookByTitleAndAuthor() {
        Book book = new Book("Гарри Поттер и Философский камень");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        Book bookn = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname()).get();
        assertThat(bookn)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrProperty("author")
                .hasFieldOrProperty("genre");

        assertAll("bookn", () -> {
            assertThat(bookn.getAuthor().getName()).isEqualTo(author.getName());
            assertThat(bookn.getAuthor().getSurname()).isEqualTo(author.getSurname());
            assertThat(bookn.getGenre().getType()).isEqualTo(genre.getType());
        });
    }

    @Test
    @DisplayName("не находит книгу по автору и названию при несовпадении автора")
    void doesNotFindBookByTitleAndAuthorWithWrongAuthor() {
        assertThat(bookRepository.findByTitleAndAuthor("Гарри Поттер и Философский камень", "Харуки", "Мураками")).isEmpty();
    }

    @Test
    @DisplayName("не находит книгу по автору и названию при несовпадении наименования")
    void doesNotFindBookByTitleAndAuthorWithWrongTitle() {
        assertThat(bookRepository.findByTitleAndAuthor("Гарри Поттер и Дары смерти", "Джоан", "Роулинг")).isEmpty();
    }

    @Test
    @DisplayName("корректно находит книги по жанру")
    void correctlyFindBookByGenre() {
        List<Book> booksByG = bookRepository.findByGenre("фэнтези");
        assertThat(booksByG).isNotEmpty().contains(bookRepository.findById(1L).get());
        assertThat(bookRepository.findByGenre("журнал").isEmpty());
    }

    @Test
    @DisplayName("корректно находит книги по автору")
    void correctlyFindBookByAuthor() {
        assertThat(bookRepository.findByAuthor("Стивен", "Кинг")).isNotEmpty();
        assertThat(bookRepository.findByAuthor("Алексей", "Пехов").isEmpty());
    }

    @DisplayName("находит книгу по автору за один селект")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findBookByAuthorForOneSelect() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        bookRepository.findByAuthor("Стивен", "Кинг");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @DisplayName("находит книгу по жанру за один селект")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findBookByGenreForOneSelect() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        bookRepository.findByGenre("фэнтези");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @DisplayName("находит книгу по жанру за один селект")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findBookByTitleAndAuthorForOneSelect() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        bookRepository.findByTitleAndAuthor("Гарри Поттер и Философский камень", "Харуки", "Мураками");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

}