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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют:")
@JdbcTest
@Import(AuthorDaoJdbc.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class AuthorDaoJdbcTest {

    @Autowired
    private AuthorDaoJdbc authorDaoJdbc;

    @Test
    @DisplayName("корректно создает нового автора")
    void correctlyInsertNewAuthor() {
        Author author = new Author("Рэй", "Брэдбери");
        assertThat(authorDaoJdbc.insert(author))
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", author.getName())
                .hasFieldOrPropertyWithValue("surname", author.getSurname());
    }

    @Test
    @DisplayName("корректно вставляет того же самого автора")
    void correctlyInsertTheSameAuthor() {
        Author author = new Author("Рэй", "Брэдбери");
        int count1 = authorDaoJdbc.count();
        authorDaoJdbc.insert(author);
        assertThat(authorDaoJdbc.count()).isEqualTo(count1 + 1);
        authorDaoJdbc.insert(author);
        assertThat(authorDaoJdbc.count()).isEqualTo(count1 + 2);
    }

    @Test
    @DisplayName("корректно обновляет автора")
    void correctlyUpdateAuthor() {
        Author author = new Author("Рэй", "Брэдбери");
        Author authorNew = authorDaoJdbc.insert(author);
        authorNew.setName("Вася");
        authorDaoJdbc.update(authorNew);
        assertThat(authorDaoJdbc.findById(authorNew.getId()).get())
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", authorNew.getName())
                .hasFieldOrPropertyWithValue("surname", authorNew.getSurname());
    }

    @Test
    @DisplayName("кидает исключение при нулевом авторе")
    void correctlyThrowExceptions() {
        assertThrows(IllegalArgumentException.class, () -> {
            authorDaoJdbc.insert(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            authorDaoJdbc.update(null);
        });
    }

    @Test
    @DisplayName("корректно удаляет автора")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteById() {
        int count1 = authorDaoJdbc.count();
        Author author = authorDaoJdbc.findById(1L).get();
        authorDaoJdbc.deleteById(1L);
        assertThat(authorDaoJdbc.count()).isEqualTo(count1 - 1);
        assertThat(authorDaoJdbc.findAll()).doesNotContain(author);
        assertDoesNotThrow(() -> authorDaoJdbc.deleteById(321L));
    }

    @Test
    @DisplayName("корректно выдает всех авторов")
    void correctlyFindingAllAuthors() {
        int count1 = authorDaoJdbc.count();
        assertThat(authorDaoJdbc.findAll().size()).isNotNull().isEqualTo(count1);
    }

    @Test
    @DisplayName("корректно находит автора по id и не кидает исключение при отсуствии id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindById() {
        Author author = new Author("Джоан", "Роулинг");
        assertThat(authorDaoJdbc.findById(1L).get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", author.getName())
                .hasFieldOrPropertyWithValue("surname", author.getSurname());
        assertDoesNotThrow(() -> { assertThat(authorDaoJdbc.findById(312)).isEmpty(); });
    }

    @Test
    @DisplayName("корректно находит автора по имени и фамилии и не кидает исключение при отсуствии автора")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindByFullName() {
        Author author = new Author("Джоан", "Роулинг");
        assertThat(authorDaoJdbc.findByFullName(author.getName(), author.getSurname())).get()
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", author.getName())
                .hasFieldOrPropertyWithValue("surname", author.getSurname());

        assertDoesNotThrow(() -> { assertThat(authorDaoJdbc.findByFullName("Алексей", "Пехов")).isEmpty(); });
    }
}