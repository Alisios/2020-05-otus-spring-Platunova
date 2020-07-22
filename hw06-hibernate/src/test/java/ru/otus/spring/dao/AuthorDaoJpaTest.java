package ru.otus.spring.dao;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тесты проверяют, что репозиторий авторов:")
@DataJpaTest
@Import(AuthorDaoJpa.class)
@Transactional
class AuthorDaoJpaTest {

    final static int INITIAL_NUMBER = 3;

    @Autowired
    private AuthorDaoJpa authorDaoHibernate;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("корректно создает нового автора")
    void correctlyInsertNewAuthor() {
        val author = new Author("Рэй", "Брэдбери");
        assertThat(authorDaoHibernate.insert(author))
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", author.getName())
                .hasFieldOrPropertyWithValue("surname", author.getSurname());
    }

    @Test
    @DisplayName("корректно вставляет того же самого автора")
    void correctlyInsertTheSameAuthor() {
        val author = new Author("Рэй", "Брэдбери");
        long count1 = authorDaoHibernate.count();
        authorDaoHibernate.insert(author);
        assertThat(authorDaoHibernate.count()).isEqualTo(count1 + 1);
    }

    @Test
    @DisplayName("корректно обновляет автора")
    void correctlyUpdateAuthor() {
        val author = new Author("Рэй", "Брэдбери");
        val authorNew = authorDaoHibernate.insert(author);
        em.detach(author);
        authorNew.setName("Вася");
        authorDaoHibernate.update(authorNew);
        assertThat(authorDaoHibernate.findById(authorNew.getId()).get())
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", authorNew.getName())
                .hasFieldOrPropertyWithValue("surname", authorNew.getSurname());
    }

    @Test
    @DisplayName("кидает исключение при нулевом авторе")
    void correctlyThrowExceptions() {
        assertThrows(NullPointerException.class, () -> authorDaoHibernate.insert(null));
        assertThrows(NullPointerException.class, () -> authorDaoHibernate.update(null));
    }

    @Test
    @DisplayName("корректно удаляет автора")
    void correctlyDeleteById() {
        long count1 = authorDaoHibernate.count();
        val author = authorDaoHibernate.findById(1L).get();
        authorDaoHibernate.deleteById(1L);
        assertThat(authorDaoHibernate.count()).isEqualTo(count1 - 1);
        assertThat(authorDaoHibernate.findAll()).doesNotContain(author);
        assertDoesNotThrow(() -> authorDaoHibernate.deleteById(321L));
    }

    @Test
    @DisplayName("корректно выдает всех авторов")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindingAllAuthors() {
        assertThat(authorDaoHibernate.findAll().size()).isNotNull().isEqualTo(INITIAL_NUMBER);
    }

    @Test
    @DisplayName("корректно считает всех авторов")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyCountAllAuthors() {
        assertThat(authorDaoHibernate.count()).isNotNull().isEqualTo(INITIAL_NUMBER);
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
    @DisplayName("корректно загружеает автора по id из БД")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindByIdFromDB() {
        val optionalActualAuthor = authorDaoHibernate.findById(1L);
        val expectedAuthor = em.find(Author.class, 1L);
        assertThat(optionalActualAuthor).isPresent().get().isEqualToComparingFieldByField(expectedAuthor);
    }


    @Test
    @DisplayName("корректно находит автора по имени и фамилии и не кидает исключение при отсуствии автора")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindByFullName() {
        val author = new Author("Джоан", "Роулинг");
        assertThat(authorDaoHibernate.findByFullName(author.getName(), author.getSurname())).get()
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", author.getName())
                .hasFieldOrPropertyWithValue("surname", author.getSurname());

        assertDoesNotThrow(() -> assertThat(authorDaoHibernate.findByFullName("Алексей", "Пехов")).isEmpty());
    }
}