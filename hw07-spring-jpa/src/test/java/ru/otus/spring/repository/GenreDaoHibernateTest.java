package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют, что репозиторий жанров:")
@DataJpaTest
@Transactional
class GenreDaoHibernateTest {

    @Autowired
    private GenreRepository genreDaoHibernate;

    @Test
    @DisplayName("кидает исключение при нулевом жанре")
    void correctlyThrowExceptions() {
        assertThrows(DataAccessException.class, () -> {
            genreDaoHibernate.save(null);
        });
        assertThrows(DataAccessException.class, () -> {
            genreDaoHibernate.save(null);
        });
    }

    @Test
    @DisplayName("корректно удаляет жанр")
    void correctlyDeleteById() {
        long count1 = genreDaoHibernate.count();
        Genre genre = genreDaoHibernate.findById(1L).get();
        genreDaoHibernate.deleteById(1L);
        assertThat(genreDaoHibernate.count()).isEqualTo(count1 - 1);
        assertThat(genreDaoHibernate.findAll()).doesNotContain(genre);
        assertThrows(DataAccessException.class, () -> {
            genreDaoHibernate.deleteById(321L);
        });
    }

    @Test
    @DisplayName("корректно находит жанр по id и не кидает исключение при отсуствии id")
    void correctlyFindById() {
        Genre genre = new Genre("фэнтези");
        assertThat(genreDaoHibernate.findById(1L).get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("type", genre.getType());
        assertDoesNotThrow(() -> {
            assertThat(genreDaoHibernate.findById(312)).isEmpty();
        });
    }

    @Test
    @DisplayName("корректно находит жанр по типу и не кидает исключение при отсуствии типа")
    void correctlyFindByType() {
        Genre genre = new Genre("фэнтези");
        assertThat(genreDaoHibernate.findByType(genre.getType())).get()
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("type", genre.getType());

        assertDoesNotThrow(() -> {
            assertThat(genreDaoHibernate.findByType("что-то неизвестное")).isEmpty();
        });
    }
}