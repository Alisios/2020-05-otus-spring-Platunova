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
import ru.otus.spring.dao.DbException;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют:")
class GenreServiceImplTest {


    @Configuration
    @Import(GenreServiceImpl.class)
    static class NestedConfiguration {}

    @MockBean
    private GenreDao genreDao;

    @Autowired
    private GenreService genreService;

    @Test
    @DisplayName("корректно создает нового атвора при отсуствии его в таблице")
    void correctlyCreateGenreIfDoesNotExist() {
        Genre genre = new Genre("Сказка");
        Genre genre2 = new Genre(1, "Сказка");
        when(genreDao.findByType(genre.getType())).thenReturn(Optional.empty());
        when(genreDao.insert(genre)).thenReturn(genre2);
        assertThat(genreService.create(genre)).isEqualTo(genre2);
        verify(genreDao, times(1)).insert(genre);
    }

    @Test
    @DisplayName("корректно обрабатывает запрос создания при наличии автора в таблице")
    void correctlyCreateGenreIfExists() {
        Genre genre = new Genre("Сказка");
        Genre genre2 = new Genre(1, "Сказка");
        when(genreDao.findByType(genre.getType())).thenReturn(Optional.of(genre2));
        assertThat(genreService.create(genre)).isEqualTo(genre2);
        verify(genreDao, times(0)).insert(genre);
    }

    @Test
    @DisplayName("корректно обрабатывает исключение дб")
    void correctlyHandleDBException() {
        Genre genre = new Genre("Сказка");
        doThrow(PermissionDeniedDataAccessException.class).when(genreDao).update(genre);
        Throwable thrown = assertThrows(DbException.class, () -> {
            genreService.save(genre);
        });
        assertThat(thrown).hasMessageContaining("Error with updating genre").hasMessageContaining("Сказка");
    }

    @Test
    @DisplayName("корректно обрабатывает запрос удаления")
    void correctlyDeleteGenre() {
        genreService.deleteById(1);
        verify(genreDao, times(1)).deleteById(1);
        doThrow(PermissionDeniedDataAccessException.class).when(genreDao).deleteById(1);
        Throwable thrown = assertThrows(DbException.class, () -> {
            genreService.deleteById(1);
        });
        assertThat(thrown).hasMessageContaining("Error with deleting genre").hasMessageContaining("1");
    }
}