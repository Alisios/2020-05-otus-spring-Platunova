package ru.otus.spring.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тесты проверяют, что репозиторий авторов:")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class AuthorDaoHibernateTest {

    @Autowired
    private AuthorRepository authorDaoHibernate;

    @Test
    @DisplayName("кидает исключение при нулевом авторе")
    void correctlyThrowExceptions() {
        assertThrows(DataAccessException.class, () -> authorDaoHibernate.save(null));
        assertThrows(DataAccessException.class, () -> authorDaoHibernate.save(null));
    }

    @Test
    @DisplayName("корректно удаляет автора")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteById() {
        long count1 = authorDaoHibernate.count();
        val author = authorDaoHibernate.findById(1L).get();
        authorDaoHibernate.deleteById(1L);
        assertThat(authorDaoHibernate.count()).isEqualTo(count1 - 1);
        assertThat(authorDaoHibernate.findAll()).doesNotContain(author);
        assertThrows(DataAccessException.class, () -> authorDaoHibernate.deleteById(321L));
    }

    @Test
    @DisplayName("корректно находит автора по id и не кидает исключение при отсуствии id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindById() {
        val author = new Author("Джоан", "Роулинг");
        assertThat(authorDaoHibernate.findById(1L).get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", author.getName())
                .hasFieldOrPropertyWithValue("surname", author.getSurname());
        assertDoesNotThrow(() -> assertThat(authorDaoHibernate.findById(312)).isEmpty());
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("корректно находит автора по имени и фамилии и не кидает исключение при отсуствии автора")
    void correctlyFindByFullName() {
        val author = new Author("Джоан", "Роулинг");
        assertThat(authorDaoHibernate.findByNameAndSurname(author.getName(), author.getSurname())).get()
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", author.getName())
                .hasFieldOrPropertyWithValue("surname", author.getSurname());

        assertDoesNotThrow(() -> assertThat(authorDaoHibernate.findByNameAndSurname("Алексей", "Пехов")).isEmpty());
    }
}