package ru.otus.spring.dao;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты проверяют, что репозиторий книги:")
@DataJpaTest
@Import(BookDaoHibernate.class)
@Transactional
class BookDaoHibernateTest {

    @Autowired
    private BookDaoHibernate bookDaoHibernate;

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
        assertThat(bookDaoHibernate.insert(book))
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("genre", book.getGenre());
    }

    @Test
    @DisplayName("не создает новую книгу при уже существующей такой же")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyInsertTheSameBook() {
        Book book = new Book("Триумфальная Арка");
        Author author = new Author("Эрик Мария", "Ремарк");
        em.persistAndFlush(author);
        book.setGenre(em.find(Genre.class, 3L));
        book.setAuthor(author);
        long count1 = bookDaoHibernate.count();
        bookDaoHibernate.insert(book);
        assertThat(bookDaoHibernate.count()).isEqualTo(count1 + 1);
        bookDaoHibernate.insert(book);
        assertThat(bookDaoHibernate.count()).isEqualTo(count1 + 1);
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
        Book bookNew = bookDaoHibernate.insert(book);
        em.detach(bookNew);
        bookNew.setTitle("Вино из топора");
        bookDaoHibernate.updateByTitle(bookNew);
        assertThat(bookDaoHibernate.findById(bookNew.getId()).get())
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("title", bookNew.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("genre", bookNew.getGenre());
    }

    @Test
    @DisplayName("кидает исключение при нулевй книге")
    void correctlyThrowExceptions() {
        assertThrows(Exception.class, () -> bookDaoHibernate.insert(null));
        assertThrows(Exception.class, () -> bookDaoHibernate.updateByTitle(null));
    }

    @Test
    @DisplayName("корректно удаляет книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteBookById() {
        long count1 = bookDaoHibernate.count();
        Book book = bookDaoHibernate.findById(1L).get();
        bookDaoHibernate.deleteById(1L);
        assertThat(bookDaoHibernate.count()).isEqualTo(count1 - 1);
        assertThat(bookDaoHibernate.findAll()).doesNotContain(book);
        assertDoesNotThrow(() -> bookDaoHibernate.deleteById(321L));
    }

    @Test
    @DisplayName("корректно выдает все книги")
    void correctlyFindingAllBooks() {
        long count1 = bookDaoHibernate.count();
        System.out.println(bookDaoHibernate.findAll());
        assertThat(bookDaoHibernate.findAll().size()).isNotNull().isEqualTo(count1);
    }

    @DisplayName("загружает список всех книг с полной информацией о них (без комментариев)")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyReturnCorrectBooksListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        val books = bookDaoHibernate.findAll();
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

        val books = bookDaoHibernate.findAll();
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
        Book bookn = bookDaoHibernate.findById(1L).get();
        assertThat(bookn)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrProperty("author");
        assertThat(bookn.getAuthor().getName()).isEqualTo(book.getAuthor().getName());
        assertThat(bookn.getGenre().getType()).isEqualTo(book.getGenre().getType());
        assertDoesNotThrow(() -> assertThat(bookDaoHibernate.findById(312)).isEmpty());
    }

    @Test
    @DisplayName("корректно находит книгу по назанию и автору и не кидает исключение при их отсуствии")
    void correctlyFindBookByTitleAndAuthor() {
        Book book = new Book("Гарри Поттер и Философский камень");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        Book bookn = bookDaoHibernate.findByTitleAndAuthor(book).get();
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
        Book book = new Book("Гарри Поттер и Философский камень");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Харуки", "Мураками");
        book.setGenre(genre);
        book.setAuthor(author);
        assertThat(bookDaoHibernate.findByTitleAndAuthor(book)).isEmpty();
    }

    @Test
    @DisplayName("не находит книгу по автору и названию при несовпадении наименования")
    void doesNotFindBookByTitleAndAuthorWithWrongTitle() {
        Book book = new Book("Гарри Поттер и Дары смерти");
        Genre genre = new Genre(1, "фэнтези");
        Author author = new Author(1, "Джоан", "Роулинг");
        book.setGenre(genre);
        book.setAuthor(author);
        assertThat(bookDaoHibernate.findByTitleAndAuthor(book)).isEmpty();
    }

    @Test
    @DisplayName("корректно находит книги по жанру")
    void correctlyFindBookByGenre() {
        List<Book> booksByG = bookDaoHibernate.findByGenre(new Genre("фэнтези"));
        assertThat(booksByG).isNotEmpty().contains(bookDaoHibernate.findById(1L).get());
        assertThat(bookDaoHibernate.findByGenre(new Genre("журнал")).isEmpty());
    }

    @Test
    @DisplayName("корректно находит книги по автору")
    void correctlyFindBookByAuthor() {
        List<Book> booksByG = bookDaoHibernate.findByAuthor(new Author("Джоан", "Роулинг"));
        assertThat(booksByG).isNotEmpty().contains(bookDaoHibernate.findById(1).get());
        assertThat(bookDaoHibernate.findByAuthor(new Author("Алексей", "Пехов")).isEmpty());
    }

    @DisplayName("находит книгу по автору за один селект")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findBookByAuthorForOneSelect() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        bookDaoHibernate.findByAuthor(new Author("Стивен", "Кинг"));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @DisplayName("находит книгу по жанру за один селект")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findBookByGenreForOneSelect() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        bookDaoHibernate.findByGenre(new Genre("фэнтези"));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @DisplayName("находит книгу по жанру за один селект")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findBookByTitleAndAuthorForOneSelect() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        bookDaoHibernate.findByTitleAndAuthor(new Book("Гарри Поттер и Философский камень", new Author("Харуки", "Мураками"), null));
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }
}