package ru.otus.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.UserBookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.dto.BookDto.fromBookDto;
import static ru.otus.spring.dto.BookDto.toBookDto;

@DisplayName("Тест проверяет:")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserBookService userBookService;


    @Autowired
    private ObjectMapper objectMapper;

    private final Book book = new Book(1, "Гарри Поттер и тайная комната", new Author("Джоан", "Роулинг"), new Genre("фэнтези"));

    @DisplayName("возвращение статуса 200 и книгу")
    @Test
    public void testReturn200AndBook() throws Exception {
        when(bookService.getById(1)).thenReturn(Optional.of(book));
        mockMvc.perform(get("/book/info/1")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(toBookDto(book))));
    }


    @DisplayName("возвращение статуса 200 и списка книг")
    @Test
    public void testReturn200AndListOfBooks() throws Exception {
        when(bookService.getAll()).thenReturn(List.of(book));
        mockMvc.perform(get("/books/list"))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(toBookDto(book)))))
                .andExpect(status().isOk());
    }

    @DisplayName("правильно формирует запрос с книгой при post")
    @Test
    public void postBookCorrectly() throws Exception {
        BookDto bookDto = toBookDto(book);
        Book book1 = fromBookDto(bookDto);
        when(userBookService.addBookByUser(book1)).thenReturn(book);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/books")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("корректно обрабатывает запрос delete")
    @Test
    public void correctlyHandleDelete() throws Exception {
        mockMvc.perform(delete("/book/{id}", 1)
                .param("id", "1"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(1);
    }

    @DisplayName("возвращение статуса notFound при неуспешном поиске книги")
    @Test
    public void testReturnNotFound() throws Exception {
        mockMvc.perform(get("/book/edit/101"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("возвращение статуса notFound при неуспешном поиске книги")
    @Test
    public void testReturnBadRequest() throws Exception {
        mockMvc.perform(get("/book/101"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

}