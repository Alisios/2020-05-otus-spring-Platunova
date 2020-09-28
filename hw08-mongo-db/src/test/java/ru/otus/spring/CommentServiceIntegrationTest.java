package ru.otus.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.CommentServiceImpl;
import ru.otus.spring.service.DbException;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IntegrationTestAnnotation
public class CommentServiceIntegrationTest {

    @Configuration
    @Import({CommentServiceImpl.class})
    static class NestedConfiguration {
    }

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("корректное созданиет нового комментария")
    void correctlySaveBookIfDoesNotExistInOneTransaction() {
        long count = commentRepository.count();
        Book book = new Book("Гарри Поттер и Философский камень",
                new Author("Джоан", "Роулинг"), new Genre("фэнтези"));
        Comment comment = new Comment(book, "супер!", new Date());
        assertThat(commentService.save(comment))
                .hasFieldOrPropertyWithValue("text", "супер!")
                .hasFieldOrPropertyWithValue("book", book);
        assertThat(commentRepository.count()).isEqualTo(count + 1);
    }

    @Test
    @DisplayName("откат операции в случае исключения")
    void rollbackActionIfExceptionWasThrown() {
        long count = commentRepository.count();
        Book book = new Book("Цветы для Элжернона", null, null);
        assertThatThrownBy(() -> commentService.findByBook(book))
                .isInstanceOf(DbException.class)
                .hasMessageContaining("with finding comment by book with title");
        assertThat(commentRepository.count()).isEqualTo(count);
    }
}
