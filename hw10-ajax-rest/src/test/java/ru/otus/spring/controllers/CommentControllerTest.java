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
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.dto.CommentDto.toCommentDto;


@DisplayName("Тест проверяет:")
@WebMvcTest(CommentController.class)
class CommentControllerTest {


    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Book book = new Book(1, "Гарри Поттер и тайная комната", new Author("Джоан", "Роулинг"), new Genre("фэнтези"));
    private final Comment comment = new Comment(2, "super", book);


    @DisplayName("возвращение статуса 200 при успешном выводе списка комментариев")
    @Test
    public void testReturn200() throws Exception {
        when(commentService.findByBookId(1)).thenReturn(List.of(comment));
        mockMvc.perform(get("/book/1/comments/list")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(toCommentDto(comment)))));
    }

    @DisplayName("корректно обрабатывает запрос delete")
    @Test
    public void correctlyHandleDelete() throws Exception {
        mockMvc.perform(delete("/book/{bookId}/comment/{id}", 1, 1))
                .andExpect(status().isOk());
        verify(commentService, times(1)).deleteById(1);
    }

    @DisplayName("правильно формирует запрос с комментарием при post")
    @Test
    public void postCommentCorrectly() throws Exception {
        CommentDto commentDto = toCommentDto(comment);
        when(bookService.getById(1)).thenReturn(Optional.of(book));
        when(commentService.save(any())).thenReturn(comment);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/book/{bookId}/comments", 1)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }


    @DisplayName("возвращение статуса notFound при некорректной ссылке")
    @Test
    public void testReturnBadRequest() throws Exception {
        mockMvc.perform(get("/book/1/comment/101"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/book/101/comments"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

}