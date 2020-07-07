package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.DaoJdbcException;
import ru.otus.spring.domain.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют:")
class DbServiceAuthorImplTest {


    @Configuration("ru.otus.spring.service")
    static class NestedConfiguration {
        @MockBean
        private AuthorDao authorDao;

        @Bean
        DbServiceAuthor dbServiceAuthor() {
            return new DbServiceAuthorImpl(authorDao);
        }
    }

    @Autowired
    private DbServiceAuthor dbServiceAuthor;

    @Autowired
    private AuthorDao authorDao;

    @Test
    @DisplayName("корректное создание нового автора при отсуствии его в таблице")
    void correctlyCreateAuthorIfDoesNotExist() {
        Author author = new Author("Стивен", "Кинг");
        Author author2 = new Author(1L, "Стивен", "Кинг");
        when(authorDao.findByFullName(author.getName(), author.getSurname())).thenReturn(Optional.empty());
        when(authorDao.insert(author)).thenReturn(author2);
        assertThat(dbServiceAuthor.create(author))
                .hasNoNullFieldsOrProperties()
                .isEqualTo(author2);
        verify(authorDao, times(1)).insert(author);
    }

    @Test
    @DisplayName("корректную обработку запроса создания при наличии автора в таблице")
    void correctlyCreateAuthorIfExists() {
        Author author = new Author("Стивен", "Кинг");
        Author author2 = new Author(1L, "Стивен", "Кинг");
        when(authorDao.findByFullName(author.getName(), author.getSurname())).thenReturn(Optional.of(author2));
        assertThat(dbServiceAuthor.create(author))
                .hasNoNullFieldsOrProperties()
                .isEqualTo(author2);
        verify(authorDao, times(0)).insert(author);
    }

    @Test
    @DisplayName("корректную обработку исключения дб")
    void correctlyHandleDBException() {
        Author author = new Author("Стивен", "Кинг");
        doThrow(PermissionDeniedDataAccessException.class).when(authorDao).update(author);
        Throwable thrown = assertThrows(DaoJdbcException.class, () -> {
            dbServiceAuthor.save(author);
        });
        assertThat(thrown).hasMessageContaining("Error with updating author");
    }

    @Test
    @DisplayName("корректую обработку запроса на удаление автора")
    void deleteAuthorById() {
        dbServiceAuthor.deleteById(1);
        verify(authorDao, times(1)).deleteById(1);
    }

}