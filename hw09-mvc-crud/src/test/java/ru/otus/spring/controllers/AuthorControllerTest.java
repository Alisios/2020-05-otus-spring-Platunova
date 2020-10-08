package ru.otus.spring.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.domain.Author;
import ru.otus.spring.service.AuthorService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тест проверяет:")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("возвращение статуса 200 при успешном поиске автора")
    @Test
    public void testReturn200() throws Exception {
        when(authorService.getById(1)).thenReturn(Optional.of(new Author(1, "Джоан", "Роулинг")));
        mockMvc.perform(get("/author/1"))
                .andExpect(model().attributeExists("author"))
                .andExpect(status().isOk());
    }

    @DisplayName("перенаправление на нужную страницу после сохранения автора")
    @Test
    public void redirectAfterSaving() throws Exception {
        var author = new Author(1, "Джоан", "Роулинг");
        when(authorService.save(author)).thenReturn(author);
        mockMvc.perform(post("/author")
                .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/author/1"));
    }

    @DisplayName("перенаправление на нужную страницу после  удаления автора")
    @Test
    public void redirectAfterDeleting() throws Exception {
        mockMvc.perform(get("/author/delete/{id}", 1)
                .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));
    }

    @DisplayName("возвращение статуса notFound при неуспешном поиске автора")
    @Test
    public void testReturnNotFound() throws Exception {
        mockMvc.perform(get("/author/101"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/author/edit/101"))
                .andExpect(status().isBadRequest());
    }

}