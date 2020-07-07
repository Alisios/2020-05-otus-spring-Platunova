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
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют:")
@JdbcTest
@Import(GenreDaoJdbc.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class GenreDaoJdbcTest {

    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    @Test
    @DisplayName("корректно создает новый жанр")
    void correctlyInsertNewGenre() {
        Genre genre = new Genre("Сказка");
        assertThat(genreDaoJdbc.insert(genre))
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("type", genre.getType());
    }

    @Test
    @DisplayName("корректно вставляет тот же самый жанр")
    void correctlyInsertTheSameGenre() {
        Genre genre = new Genre("Сказка");
        int count1 = genreDaoJdbc.count();
        genreDaoJdbc.insert(genre);
        assertThat(genreDaoJdbc.count()).isEqualTo(count1 + 1);
        genreDaoJdbc.insert(genre);
        assertThat(genreDaoJdbc.count()).isEqualTo(count1 + 2);
    }

    @Test
    @DisplayName("корректно обновляет жанр")
    void correctlyUpdateGenre() {
        Genre genre = new Genre("Сказка");
        Genre genreNew = genreDaoJdbc.insert(genre);
        genreNew.setType("Сказка народная");
        genreDaoJdbc.update(genreNew);
        assertThat(genreDaoJdbc.findById(genreNew.getId()).get())
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("type", genreNew.getType());
    }

    @Test
    @DisplayName("кидает исключение при нулевом жанре")
    void correctlyThrowExceptions() {
        assertThrows(IllegalArgumentException.class, () -> {
            genreDaoJdbc.insert(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            genreDaoJdbc.update(null);
        });
    }

    @Test
    @DisplayName("корректно удаляет жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteById() {
        int count1 = genreDaoJdbc.count();
        Genre genre = genreDaoJdbc.findById(1L).get();
        genreDaoJdbc.deleteById(1L);
        assertThat(genreDaoJdbc.count()).isEqualTo(count1 - 1);
        assertThat(genreDaoJdbc.findAll()).doesNotContain(genre);
        assertDoesNotThrow(() -> genreDaoJdbc.deleteById(321L));
    }

    @Test
    @DisplayName("корректно выдает все жанры")
    void correctlyFindingAllGenres() {
        int count1 = genreDaoJdbc.count();
        assertThat(genreDaoJdbc.findAll().size()).isNotNull().isEqualTo(count1);
    }

    @Test
    @DisplayName("корректно находит жанр по id и не кидает исключение при отсуствии id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindById() {
        Genre genre = new Genre("фэнтези");
        assertThat(genreDaoJdbc.findById(1L).get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("type", genre.getType());
        assertDoesNotThrow(() -> {
            assertThat(genreDaoJdbc.findById(312)).isEmpty();
        });
    }

    @Test
    @DisplayName("корректно находит жанр по типу и не кидает исключение при отсуствии типа")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindByType() {
        Genre genre = new Genre("фэнтези");
        assertThat(genreDaoJdbc.findByType(genre.getType())).get()
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("type", genre.getType());

        assertDoesNotThrow(() -> {
            assertThat(genreDaoJdbc.findByType("что-то неизвестное")).isEmpty();
        });
    }
}