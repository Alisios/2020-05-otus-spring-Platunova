package ru.otus.spring.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.RepositoryTestAnnotation;
import ru.otus.spring.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@DisplayName("Тесты проверяют, что репозиторий авторов:")
@RepositoryTestAnnotation
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("корректно обновляет список с книгами автора")
    void correctlyUpdateAuthorListOfBook() {
        val author = authorRepository.findFirstByNameAndSurname("Алексей", "Пехов").get();
        long oldSize = author.getListOfBookTitles().size();
        author.getListOfBookTitles().add("Хроники Сиалы");
        authorRepository.updateBookTitleList(author);
        assertThat(authorRepository.findFirstByNameAndSurname("Алексей", "Пехов").get())
                .isNotNull()
                .matches(b -> b.getListOfBookTitles() != null && b.getListOfBookTitles().size() == (oldSize + 1) && b.getListOfBookTitles().contains("Хроники Сиалы"));
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("корректно находит автора по имени и фамилии и не кидает исключение при отсуствии автора")
    void correctlyFindByFullName() {
        val author = new Author("Джоан", "Роулинг");
        assertThat(authorRepository.findFirstByNameAndSurname(author.getName(), author.getSurname())).get()
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", author.getName())
                .hasFieldOrPropertyWithValue("surname", author.getSurname());

        assertDoesNotThrow(() -> assertThat(authorRepository.findFirstByNameAndSurname("Эрих Мария", "Ремарк")).isEmpty());
    }
}