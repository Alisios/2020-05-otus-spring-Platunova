package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют, что репозиторий жанров:")
@DataJpaTest
@Import(GenreDaoHibernate.class)
@Transactional
class GenreDaoHibernateTest {

    final static int INITIAL_NUMBER = 3;

    @Autowired
    private GenreDaoHibernate genreDaoHibernate;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("корректно создает новый жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyInsertNewGenre() {
        Genre genre = new Genre("Сказка");
        assertThat(genreDaoHibernate.insert(genre))
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("type", genre.getType());
    }

    @Test
    @DisplayName("корректно вставляет тот же самый жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyInsertTheSameGenre() {
        Genre genre = new Genre("Сказка");
        long count1 = genreDaoHibernate.count();
        genreDaoHibernate.insert(genre);
        assertThat(genreDaoHibernate.count()).isEqualTo(count1 + 1);
        genreDaoHibernate.insert(genre);
        assertThat(genreDaoHibernate.count()).isEqualTo(count1 + 1);
    }

    @Test
    @DisplayName("корректно обновляет жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyUpdateGenre() {
        Genre genre = new Genre("Сказка");
        Genre genreNew = genreDaoHibernate.insert(genre);
        em.detach(genre);
        genreNew.setType("Сказка народная");
        genreDaoHibernate.update(genreNew);
        assertThat(genreDaoHibernate.findById(genreNew.getId()).get())
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("type", genreNew.getType());
    }

    @Test
    @DisplayName("кидает исключение при нулевом жанре")
    void correctlyThrowExceptions() {
        assertThrows(NullPointerException.class, () -> {
            genreDaoHibernate.insert(null);
        });
        assertThrows(NullPointerException.class, () -> {
            genreDaoHibernate.update(null);
        });
    }

    @Test
    @DisplayName("корректно удаляет жанр")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteById() {
        long count1 = genreDaoHibernate.count();
        Genre genre = genreDaoHibernate.findById(1L).get();
        genreDaoHibernate.deleteById(1L);
        assertThat(genreDaoHibernate.count()).isEqualTo(count1 - 1);
        assertThat(genreDaoHibernate.findAll()).doesNotContain(genre);
        assertDoesNotThrow(() -> genreDaoHibernate.deleteById(321L));
    }

    @Test
    @DisplayName("корректно выдает все жанры")
    void correctlyFindingAllGenres() {
        assertThat(genreDaoHibernate.findAll().size()).isNotNull().isEqualTo(INITIAL_NUMBER);
    }

    @Test
    @DisplayName("корректно считает все жанры")
    void correctlyCountAllGenres() {
        assertThat(genreDaoHibernate.count()).isNotNull().isEqualTo(INITIAL_NUMBER);
    }

    @Test
    @DisplayName("корректно находит жанр по id и не кидает исключение при отсуствии id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
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