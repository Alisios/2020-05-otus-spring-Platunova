package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тесты проверяют, что репозиторий жанров:")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("кидает исключение при нулевом жанре")
    void correctlyThrowExceptions() {
        assertThrows(DataAccessException.class, () ->  genreRepository.save(null));
        assertThrows(DataAccessException.class, () -> genreRepository.save(null));
    }

    @Test
    @DisplayName("корректно удаляет жанр")
    void correctlyDeleteById() {
        long count1 = genreRepository.count();
        Genre genre = genreRepository.findById(1L).get();
        genreRepository.deleteById(1L);
        assertThat(genreRepository.count()).isEqualTo(count1 - 1);
        assertThat(genreRepository.findAll()).doesNotContain(genre);
        assertThrows(DataAccessException.class, () -> {
            genreRepository.deleteById(321L);
        });
    }

    @Test
    @DisplayName("корректно находит жанр по id и не кидает исключение при отсуствии id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindById() {
        Genre genre = new Genre("фэнтези");
        assertThat(genreRepository.findById(1L).get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("type", genre.getType());
        assertDoesNotThrow(() -> assertThat(genreRepository.findById(312)).isEmpty());
    }

    @Test
    @DisplayName("корректно находит жанр по типу и не кидает исключение при отсуствии типа")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindByType() {
        Genre genre = new Genre("фэнтези");
        assertThat(genreRepository.findByType(genre.getType())).get()
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("type", genre.getType());

        assertDoesNotThrow(() -> assertThat(genreRepository.findByType("что-то неизвестное")).isEmpty());
    }
}