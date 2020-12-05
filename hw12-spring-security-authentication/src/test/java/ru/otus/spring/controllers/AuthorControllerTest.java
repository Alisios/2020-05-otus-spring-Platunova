package ru.otus.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.UserAuthorisationService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.dto.AuthorDto.fromAuthorDto;

@DisplayName("Тест проверяет:")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserAuthorisationService authorisationService;

    @DisplayName("возвращение статуса 200 и нужного автора при успешном поиске автора")
    @Test
    @WithMockUser(username = "grishaabc123", authorities = {"USER"}, password = "abc123")
    public void testReturn200andAuthor() throws Exception {
        when(authorService.getById(1)).thenReturn(Optional.of(new Author(1, "Джоан", "Роулинг", Collections.emptyList())));
        mockMvc.perform(get("/author/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'name':'Джоан', 'surname':'Роулинг'}"));
    }

    @DisplayName("перенаправление на login для неаутентифицированных пользователей")
    @Test
    public void testReturn302() throws Exception {
        when(authorService.getById(1)).thenReturn(Optional.of(new Author(1, "Джоан", "Роулинг", Collections.emptyList())));
        mockMvc.perform(get("/author/1"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("правильно формирует запрос с автором при post")
    @Test
    @WithMockUser(username = "grishaabc123", authorities = {"USER"}, password = "abc123")
    public void postAuthorCorrectly() throws Exception {
        var authorDto = new AuthorDto(1, "Джоан", "Роулинг", Collections.emptyList());
        var author = fromAuthorDto(authorDto);
        when(authorService.save(author)).thenReturn(author);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/authors")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("корректно обрабатывает запрос delete")
    @Test
    @WithMockUser(username = "grishaabc123", authorities = {"USER"}, password = "abc123")
    public void correctlyHandleDelete() throws Exception {
        mockMvc.perform(delete("/author/{id}", 1)
                .param("id", "1"))
                .andExpect(status().isOk());
        verify(authorService, times(1)).deleteById(1);
    }

    @DisplayName("возвращение статуса notFound при неуспешном поиске автора")
    @Test
    @WithMockUser(username = "grishaabc123", authorities = {"USER"}, password = "abc123")
    public void testReturnNotFound() throws Exception {
        mockMvc.perform(get("/author/edit/101"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("возвращение статуса notFound при неуспешном поиске автора")
    @Test
    @WithMockUser(username = "grishaabc123", authorities = {"USER"}, password = "abc123")
    public void testReturnBadRequest() throws Exception {
        mockMvc.perform(get("/author/101"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

}