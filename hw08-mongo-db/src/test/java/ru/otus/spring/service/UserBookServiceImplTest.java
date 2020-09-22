package ru.otus.spring.service;

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
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Тесты проверяют:")
class UserBookServiceImplTest {


    @Configuration
    @Import(UserBookServiceImpl.class)
    static class NestedConfiguration {
    }

    @Autowired
    private UserBookService userBookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private IOService ioService;

    @MockBean
    private UserIOService userIOService;

    private final Genre genre = new Genre("фэнтези");
    private final Author author = new Author("Джоан", "Роулинг");
    private final Book book = new Book("Гарри Поттер и Философский камень", author, genre);

    @Test
    @DisplayName("корректно обрабатывает сообщение пользователя и добавляет книгу")
    void correctlyHandleUserMessageAndAddNewBook() {
        when(ioService.inputMessage()).thenReturn("Гарри Поттер и Философский камень").thenReturn(genre.getName());
        when(userIOService.getAuthorInfoFromUser()).thenReturn(author);
        when(authorService.save(any())).thenReturn(author);
        book.setAuthor(author);
        book.setGenre(genre);
        when(bookService.save(book)).thenReturn(book);
        assertThat(userBookService.addBookByUser()).isEqualTo(book);
    }


    @Test
    @DisplayName("корректно обрабатывает сообщение пользователя и добавляет автора")
    void correctlyHandleUserMessageAndAddNewAuthor() {
        Author author = new Author("Рэй", "Брэдбери");
        when(userIOService.getAuthorInfoFromUser()).thenReturn(author);
        when(authorService.save(author)).thenReturn(author);
        assertThat(userBookService.addAuthorByUser()).isEqualTo(author);
    }

    @Test
    @DisplayName("корректно обрабатывает сообщение пользователя и добавляет комментарий")
    void correctlyHandleUserMessageAndAddNewComment() {
        when(userIOService.getBookInfoFromUser()).thenReturn(book);
        var comment = new Comment(book, "крутая!");
        when(ioService.inputMessage()).thenReturn("крутая!").thenReturn(comment.getText());
        when(userIOService.getAuthorInfoFromUser()).thenReturn(author);
        when(commentService.save(comment)).thenReturn(comment);
        when(bookService.getByTitleAndAuthor(book)).thenReturn(Optional.of(book));
        when(commentService.findByBook(book)).thenReturn(List.of(comment));
        assertThat(userBookService.addCommentByUser()).isEqualTo(comment);
    }

    @Test
    @DisplayName("корректно отображает книги вместе с комментариями")
    void correctlyPrintBooksWithComments() {
        var comment = new Comment(book, "крутая!");
        when(userIOService.getBookInfoFromUser()).thenReturn(book);
        when(bookService.getByTitleAndAuthor(book)).thenReturn(Optional.of(book));
        when(commentService.findByBook(book)).thenReturn(List.of(comment));
        assertThat(userBookService.printBooksWithComments()).contains(comment.toStringWithoutBook()).contains(book.toString());
    }
}