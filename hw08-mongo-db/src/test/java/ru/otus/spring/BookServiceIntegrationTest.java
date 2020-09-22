package ru.otus.spring;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.service.*;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTestAnnotation
public class BookServiceIntegrationTest {

    @Configuration
    @Import({BookServiceImpl.class, CommentServiceImpl.class, AuthorServiceImpl.class})
    static class NestedConfiguration {
    }

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    BookService bookService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthorService authorService;


    @Test
    @DisplayName("корректное создание новой книги ")
    void correctlySaveBookIfDoesNotExist() {
        val book = new Book("Норвежский Лес");
        val author = new Author("Харуки", "Мураками");
        book.setAuthor(author);
        val genre = new Genre("роман");
        book.setGenre(genre);
        long count = bookRepository.count();
        assertThat(bookService.save(book))
                .hasFieldOrPropertyWithValue("title", "Норвежский Лес")
                .hasFieldOrPropertyWithValue("author", author)
                .hasFieldOrPropertyWithValue("genre", genre);
        assertThat(bookRepository.count()).isEqualTo(count + 1);
    }

    @Test
    @DisplayName("корректный откат в случае исключения")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void rollbackActionIfExceptionWasThrown() {
        long count = bookRepository.count();
        assertThatThrownBy(() -> bookService.save(null))
                .isInstanceOf(DbException.class)
                .hasMessageContaining("Error with saving");
        assertThat(bookRepository.count()).isEqualTo(count);
    }

    @Test
    @DisplayName("корректную обработку взаимосвязанных между репозиториями операциями, включая удаление всех книг по автору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyHandleAllOperationsBetweenRepositories() {
        var book = new Book("Норвежский Лес");
        var book2 = new Book("В стране чудес без тормозов");
        var author = new Author("Харуки", "Мураками");
        author.getListOfBookTitles().add(book.getTitle());
        author.getListOfBookTitles().add(book2.getTitle());

        long count = bookRepository.count();
        long countA = authorRepository.count();
        long countC = commentRepository.count();

        author = authorService.save(author);
        book.setAuthor(author);
        book2.setAuthor(author);

        val genre = new Genre("роман");
        book.setGenre(genre);
        book2.setGenre(genre);
        bookService.save(book);
        bookService.save(book2);

        var comment1 = new Comment(book, "супер!!!", new Date());
        comment1 = commentService.save(comment1);
        commentService.save(new Comment(book, "супер2!", new Date()));
        commentService.save(new Comment(book, "супер3!", new Date()));

        var comment2 = new Comment(book2, "просто класс!", new Date());
        comment2 = commentService.save(comment2);
        commentService.save(new Comment(book2, "супер2!", new Date()));
        commentService.save(new Comment(book2, "супер3!", new Date()));

        assertThat(commentService.findByBook(book))
                .hasSize(3).contains(comment1).doesNotContain(comment2);

        assertThat(commentService.findByBook(book2))
                .hasSize(3).contains(comment2).doesNotContain(comment1);

        assertThat(authorRepository.findById(author.getId()).get().getListOfBookTitles()).hasSize(2);
        assertThat(authorRepository.count()).isEqualTo(countA + 1);
        assertThat(bookRepository.count()).isEqualTo(count + 2);
        assertThat(commentRepository.count()).isEqualTo(countC + 6);

        bookService.deleteBookByAuthor(author.getName(), author.getSurname());

        assertThat(authorRepository.findById(author.getId()).get().getListOfBookTitles()).isEmpty();
        assertThat(authorRepository.count()).isEqualTo(countA + 1);
        assertThat(bookRepository.count()).isEqualTo(count);
        assertThat(commentRepository.count()).isEqualTo(countC);
    }

    @Test
    @DisplayName("корректную обрабатку взаимосвязанных между репозиториями операций, включая удаление 1 книги по автору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyHandleAllOperationsBetweenRepositories2() {
        var book = new Book("Норвежский Лес");
        var book2 = new Book("В стране чудес без тормозов");
        var author = new Author("Харуки", "Мураками");
        author.getListOfBookTitles().add(book.getTitle());
        author.getListOfBookTitles().add(book2.getTitle());
        long count = bookRepository.count();
        long countA = authorRepository.count();
        long countC = commentRepository.count();

        author = authorService.save(author);

        book.setAuthor(author);
        book2.setAuthor(author);
        val genre = new Genre("роман");
        book.setGenre(genre);
        book2.setGenre(genre);

        bookService.save(book);
        bookService.save(book2);

        commentService.save(new Comment(book, "супер!!!", new Date()));
        commentService.save(new Comment(book, "супер2!", new Date()));
        commentService.save(new Comment(book, "супер3!", new Date()));

        commentService.save(new Comment(book2, "просто класс!", new Date()));
        commentService.save(new Comment(book2, "супер2!", new Date()));
        commentService.save(new Comment(book2, "супер3!", new Date()));

        assertThat(authorRepository.findById(author.getId()).get().getListOfBookTitles()).hasSize(2);
        assertThat(authorRepository.count()).isEqualTo(countA + 1);
        assertThat(bookRepository.count()).isEqualTo(count + 2);
        assertThat(commentRepository.count()).isEqualTo(countC + 6);

        bookService.deleteByTitleAndAuthor(book);

        assertThat(authorRepository.findById(author.getId()).get().getListOfBookTitles())
                .hasSize(1)
                .contains(book2.getTitle());
        assertThat(authorRepository.count()).isEqualTo(countA + 1);
        assertThat(bookRepository.count()).isEqualTo(count + 1);
        assertThat(commentRepository.count()).isEqualTo(countC + 3);
    }

}
