package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.PermissionDeniedDataAccessException;
import ru.otus.spring.dao.DaoJdbcException;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют:")
class DbServiceGenreImplTest {

    @Configuration("ru.otus.spring.service")
    static class NestedConfiguration {
        @MockBean
        private GenreDao genreDao;

        @Bean
        DbServiceGenre dbServiceGenre() {
            return new DbServiceGenreImpl(genreDao);
        }
    }

    @Autowired
    private DbServiceGenre dbServiceGenre;

    @Autowired
    private GenreDao genreDao;

    @Test
    @DisplayName("корректно создает нового атвора при отсуствии его в таблице")
    void correctlyCreateGenreIfDoesNotExist() {
        Genre genre = new Genre("Сказка");
        Genre genre2 = new Genre(1, "Сказка");
        when(genreDao.findByType(genre.getType())).thenReturn(Optional.empty());
        when(genreDao.insert(genre)).thenReturn(genre2);
        assertThat(dbServiceGenre.create(genre))
                .hasNoNullFieldsOrProperties()
                .isEqualTo(genre2);
        verify(genreDao, times(1)).insert(genre);
    }

    @Test
    @DisplayName("корректно обрабатывает запрос создания при наличии автора в таблице")
    void correctlyCreateGenreIfExists() {
        Genre genre = new Genre("Сказка");
        Genre genre2 = new Genre(1, "Сказка");
        when(genreDao.findByType(genre.getType())).thenReturn(Optional.of(genre2));
        assertThat(dbServiceGenre.create(genre))
                .hasNoNullFieldsOrProperties()
                .isEqualTo(genre2);
        verify(genreDao, times(0)).insert(genre);
    }

    @Test
    @DisplayName("корректно обрабатывает исключение дб")
    void correctlyHandleDBException() {
        Genre genre = new Genre("Сказка");
        doThrow(PermissionDeniedDataAccessException.class).when(genreDao).update(genre);
        Throwable thrown = assertThrows(DaoJdbcException.class, () -> {
            dbServiceGenre.save(genre);
        });
        assertThat(thrown).hasMessageContaining("Error with updating genre").hasMessageContaining("Сказка");
    }

    @Test
    @DisplayName("корректно обрабатывает запрос удаления")
    void correctlyDeleteGenre() {
        dbServiceGenre.deleteById(1);
        verify(genreDao, times(1)).deleteById(1);
        doThrow(PermissionDeniedDataAccessException.class).when(genreDao).deleteById(1);
        Throwable thrown = assertThrows(DaoJdbcException.class, () -> {
            dbServiceGenre.deleteById(1);
        });
        assertThat(thrown).hasMessageContaining("Error with deleting genre").hasMessageContaining("1");
    }
}