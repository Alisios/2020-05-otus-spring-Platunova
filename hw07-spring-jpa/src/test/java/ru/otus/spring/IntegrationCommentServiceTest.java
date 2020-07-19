package ru.otus.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.DbException;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.CommentServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("Интеграционный тест проверяет:")
@EnableJpaRepositories(basePackages = "ru.otus.spring.repository")
@EntityScan("ru.otus.spring.domain")
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class IntegrationCommentServiceTest {

    @Configuration
    @Import({CommentServiceImpl.class})
    static class NestedConfiguration {}

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("корректно создает новый комментарий за одну транзакцию")
    void correctlySaveBookIfDoesNotExistInOneTransaction() {
        long count = commentRepository.count();
        Book book = new Book ("Гарри Поттер и Философский камень",
                new Author("Джоан", "Роулинг"), new Genre("фэнтези"));
        Comment comment = new Comment(book,"супер!");
        assertThat(commentService.save(comment))
                .hasFieldOrPropertyWithValue("text", "супер!")
                .hasFieldOrPropertyWithValue("book", book);
         assertThat(commentRepository.count()).isEqualTo(count + 1);
    }

    @Test
    @DisplayName("корректно находит комментарий по книги за одну транзакцию")
    void correctlyFindCommentByBookInOneTransaction() {
        Comment comment = commentRepository.findById(1).get();
        assertThat(commentService.findByBook(new Book ("Гарри Поттер и Философский камень",
                new Author("Джоан", "Роулинг"), new Genre("фэнтези"))))
                .isNotEmpty()
                .hasSize(4);//.contains(comment);

    }

    @Test
    @DisplayName("откатывает транзакцию в слчае исключения")
    void rollbackActionIfExceptionWasThrown() {
        long count = commentRepository.count();
        Book book = new Book("Цветы для Элжернона" , null, null);
        assertThatThrownBy(()->commentService.findByBook(book))
                .isInstanceOf(DbException.class)
                .hasMessageContaining("with finding comment by book with title");
        assertThat(commentRepository.count()).isEqualTo(count);
    }

}
