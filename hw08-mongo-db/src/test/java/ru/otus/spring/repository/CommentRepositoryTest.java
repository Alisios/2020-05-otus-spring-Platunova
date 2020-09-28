package ru.otus.spring.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.RepositoryTestAnnotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Тесты проверяют, что репозиторий комментариев:")
@RepositoryTestAnnotation
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("кидает исключение при нулевом комментарии")
    void correctlyThrowExceptions() {
        assertThrows(Exception.class, () -> commentRepository.save(null));
    }

    @Test
    @DisplayName("корректно удаляет комментарий по названию книги, автору и комментарию")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteCommentByTitleAndAuthorAndText() {
        long count1 = commentRepository.count();
        var comment = commentRepository.findAllByTextContainingIgnoreCase("феникса").get(0);
        commentRepository.deleteByBook_TitleIgnoreCaseAndBook_Author_NameIgnoreCaseAndBook_Author_SurnameIgnoreCaseAndAndTextContainingIgnoreCase("Гарри Поттер и тайная комната", "Джоан", "Роулинг", "феникса");
        assertThat(commentRepository.count()).isEqualTo(count1 - 1);
        assertThat(commentRepository.findAll()).doesNotContain(comment);
    }

    @Test
    @DisplayName("корректно находит комментарии по названию книги и автору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteCommentByTitleAndAuthor() {
        var comments = commentRepository
                .findAllByBook_TitleIgnoreCaseAndBook_Author_NameIgnoreCaseAndBook_Author_SurnameIgnoreCase(
                        "Гарри Поттер и тайная комната",
                        "Джоан",
                        "Роулинг");
        assertThat(comments).isNotEmpty()
                .hasSizeGreaterThan(2)
                .matches(b -> b.stream().anyMatch(b1 -> b1.getText().contains("Классная книга")));//("Классная книга!")));

    }

    @Test
    @DisplayName("корректно удаляет все комментарии по книге")
    void deleteAllCommentsByBook() {
        long count1 = commentRepository.count();
        var comments = commentRepository
                .findAllByBook_TitleIgnoreCaseAndBook_Author_NameIgnoreCaseAndBook_Author_SurnameIgnoreCase(
                        "Гарри Поттер и тайная комната",
                        "Джоан",
                        "Роулинг");
        commentRepository.deleteAllByBook("Гарри Поттер и тайная комната", "Джоан", "Роулинг");
        assertThat(commentRepository.count()).isEqualTo(count1 - comments.size());
    }

    @DisplayName("загружает список всех книг с полной информацией о них (без комментариев)")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyReturnCorrectCommentsListWithAllInfo() {
        var comments = commentRepository.findAll();
        assertThat(comments).isNotNull().hasSizeGreaterThan(5)
                .allMatch(s -> !s.getText().equals(""))
                .allMatch(s -> s.getBook() != null);
    }

    @DisplayName("корректно обрабатывает комментарий по тексту ")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findByTextWithBook() {
        var comments = commentRepository.findAllByTextContainingIgnoreCase("классная");
        var comments2 = commentRepository.findAllByTextContainingIgnoreCase("КлаССная ");
        assertThat(comments.get(0))
                .hasFieldOrPropertyWithValue("text", "Классная книга!")
                .hasFieldOrPropertyWithValue("book.title", "Гарри Поттер и тайная комната");
        assertThat(comments).isEqualTo(comments2);
        assertThat(commentRepository.findAllByTextContainingIgnoreCase("dsdfa")).isEmpty();
    }

}