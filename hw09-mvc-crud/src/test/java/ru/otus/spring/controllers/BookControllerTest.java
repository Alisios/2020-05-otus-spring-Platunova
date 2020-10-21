package ru.otus.spring.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.ServiceException;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.UserBookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тест проверяет:")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @MockBean
    private  UserBookService userBookService;

    @Autowired
    private MockMvc mockMvc;

    private final Book book = new Book(1, "Гарри Поттер и тайная комната", new Author("Джоан", "Роулинг"), new Genre("фэнтези"));

    @DisplayName("возвращение статуса 200 при успешных crud операциях")
    @Test
    public void testReturn200() throws Exception {
        when(bookService.getById(1)).thenReturn(Optional.of(book));
        mockMvc.perform(get("/book/1"))
                .andExpect(model().attributeExists("book"))
                .andExpect(status().isOk());

        when(bookService.getAll()).thenReturn(List.of());
        mockMvc.perform(get("/books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/book/edit/{id}", 1)
                .param("id", "1"))
                .andExpect(model().attributeExists("book"))
                .andExpect(status().isOk());
    }


    @DisplayName("перенаправление на нужную страницу после сохранения книги")
    @Test
    public void redirectAfterSaving() throws Exception {
        when(userBookService.addBookByUser(book)).thenReturn(book);
        mockMvc.perform(post("/book")
                .param("id", "1")
                .flashAttr("book.title", book.getTitle())
                .flashAttr("book.author.name", book.getAuthor().getName())
                .flashAttr("book.author.surname", book.getAuthor().getSurname()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/1"));
    }

    @DisplayName("перенаправление на нужную страницу после удаления книги")
    @Test
    public void redirectAfterDeleting() throws Exception {
        mockMvc.perform(get("/book/delete/{id}", 1)
                .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @DisplayName("возвращение статуса notFound при неуспешном поиске книги")
    @Test
    public void testReturnNotFound() throws Exception {
        mockMvc.perform(get("/book/101"))
                .andExpect(status().isBadRequest());

        when(bookService.save(book)).thenThrow(ServiceException.class);
        mockMvc.perform(get("/book/1"))
                .andExpect(status().isBadRequest());
    }

}