package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.DbException;

import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют, что сервис с жанрами:")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class DbServiceGenreImplTest {

    @Configuration
    @Import(DbServiceGenreImpl.class)
    static class NestedConfiguration {}

    @MockBean
    private GenreRepository genreDao;

    @Autowired
    private DbServiceGenre dbServiceGenre;

    @Test
    @DisplayName("корректно создает нового атвора при отсуствии его в таблице")
    void correctlySaveGenreIfDoesNotExist() {
        Genre genre = new Genre("Сказка");
        Genre genre2 = new Genre(1, "Сказка");
        when(genreDao.findByType(genre.getType())).thenReturn(Optional.empty());
        when(genreDao.save(genre)).thenReturn(genre2);
        assertThat(dbServiceGenre.save(genre)).isEqualTo(genre2);
        verify(genreDao, times(1)).save(genre);
    }

    @Test
    @DisplayName("корректно обрабатывает запрос создания при наличии автора в таблице")
    void correctlySaveGenreIfExists() {
        Genre genre = new Genre("Сказка");
        Genre genre2 = new Genre(1, "Сказка");
        when(genreDao.findByType(genre.getType())).thenReturn(Optional.of(genre2));
        assertThat(dbServiceGenre.save(genre)).isEqualTo(genre2);
        verify(genreDao, times(0)).save(genre);
    }

    @Test
    @DisplayName("корректно обрабатывает исключение дб")
    void correctlyHandleDBException() {
        Genre genre = new Genre("Сказка");
        doThrow(PermissionDeniedDataAccessException.class).when(genreDao).save(genre);
        Throwable thrown = assertThrows(DbException.class, () -> {
            dbServiceGenre.save(genre);
        });
        assertThat(thrown).hasMessageContaining("Error with saving genre").hasMessageContaining("Сказка");
    }

    @Test
    @DisplayName("корректно обрабатывает запрос удаления")
    void correctlyDeleteGenre() {
        dbServiceGenre.deleteById(1);
        verify(genreDao, times(1)).deleteById(1);
        doThrow(PermissionDeniedDataAccessException.class).when(genreDao).deleteById(1);
        Throwable thrown = assertThrows(DbException.class, () -> {
            dbServiceGenre.deleteById(1);
        });
        assertThat(thrown).hasMessageContaining("Error with deleting genre").hasMessageContaining("1");
    }
}