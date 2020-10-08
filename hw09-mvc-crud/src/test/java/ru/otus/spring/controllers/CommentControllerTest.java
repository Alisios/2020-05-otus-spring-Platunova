package ru.otus.spring.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.ServiceException;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("Тест проверяет:")
@WebMvcTest(CommentController.class)
class CommentControllerTest {


    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    private final Book book = new Book(1, "Гарри Поттер и тайная комната", new Author("Джоан", "Роулинг"), new Genre("фэнтези"));
    private final Comment comment = new Comment(2, "super", book);


    @DisplayName("возвращение статуса 200 при успешных crud операциях")
    @Test
    public void testReturn200() throws Exception {
        when(commentService.findByBookId(1)).thenReturn(List.of(comment));
        when(bookService.getById(1)).thenReturn(Optional.of(book));

        when(commentService.getAll()).thenReturn(List.of());
        mockMvc.perform(get("/book/{id}/comments", 1))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("comment"))
                .andExpect(status().isOk());

    }

    @DisplayName("перенаправление на нужную страницу после сохранения книги")
    @Test
    public void redirectAfterSaving() throws Exception {
        when(commentService.findByBookId(1)).thenReturn(List.of(comment));
        when(bookService.getById(1)).thenReturn(Optional.of(book));
        mockMvc.perform(post("/book/{bookId}/comment", 1)
                .param("id", "1")
                .flashAttr("comment.text", comment.getText())
                .flashAttr("comment.book.title", book.getTitle())
                .flashAttr("comment.book.author.name", book.getAuthor().getName())
                .flashAttr("book.author.surname", book.getAuthor().getSurname()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/1/comments"));
    }

    @DisplayName("перенаправление на нужную страницу после удаления книги")
    @Test
    public void redirectAfterDeleting() throws Exception {
        mockMvc.perform(get("/book/{bookId}/comment/delete/{id}", 1, 2)
                .param("bookId", "1")
                .param("id", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/1/comments"));
    }


    @DisplayName("возвращение статуса notFound при неуспешном поиске автора")
    @Test
    public void testReturnNotFound() throws Exception {

        when(bookService.getById(101)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/book/101/comments"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/book/101/comment"))
                .andExpect(status().isBadRequest());

        when(bookService.getById(1)).thenReturn(Optional.of(book));
        when(commentService.save(any())).thenThrow(ServiceException.class);
        mockMvc.perform(post("/book/{id}/comment", 1))
                .andExpect(status().isBadRequest());
    }
}