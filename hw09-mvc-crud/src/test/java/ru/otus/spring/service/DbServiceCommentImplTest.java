package ru.otus.spring.service;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.ServiceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Тесты проверяют, что сервис скомментариями:")
class DbServiceCommentImplTest {

    @Configuration
    @Import(CommentServiceImpl.class)
    static class NestedConfiguration {
    }

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("корректно создает новый комментарий")
    void correctlySaveCommentIfDoesNotExist() {
        val author = new Author("Харуки", "Мураками");
        val book2 = new Book(1L, "Норвежский Лес", author, null);
        val comment2 = new Comment(1, "comment", book2);
        val comment = new Comment(0, "comment", book2);
        when(commentRepository.save(comment)).thenReturn(comment2);
        assertThat(commentService.save(comment)).isEqualTo(comment2);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("корректно обрабатывает исключение дб")
    void correctlyHandleDBException() {
        doThrow(RuntimeException.class)
                .when(commentRepository).findAll();
        Throwable thrown = assertThrows(ServiceException.class, () -> {
            commentService.getAll();
        });
        assertThat(thrown).hasMessageContaining("Error with finding all comment");
    }
}