package ru.otus.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.otus.spring.domain.User;
import ru.otus.spring.service.UserAuthorisationService;
import ru.otus.spring.service.UserService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("Тест проверяет:")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAuthorisationService authorisationService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("возвращение статуса 200 и нужного пользователя при админских правах")
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"}, password = "11111")
    public void testReturn200andUserWhenAdmin() throws Exception {
        when(userService.getById(1)).thenReturn(Optional.of(new User(1, "Джоан", "Роулинг", "joan", "$2y$12$161FmGdY386kQZpF1do4i.yMAxcKUBRVl5xNyoSOtBRfqJGVj9X3a")));
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'name':'Джоан', 'surname':'Роулинг'}"));
    }

    @DisplayName("возвращение статуса 403 при опытке доступа к users не админом")
    @Test
    @WithMockUser(username = "grishaabc123", authorities = {"USER"}, password = "abc123")
    public void testReturn403NotForAdmin() throws Exception {
        when(userService.getById(1)).thenReturn(Optional.of(new User(1, "Джоан", "Роулинг", "joan", "$2y$12$161FmGdY386kQZpF1do4i.yMAxcKUBRVl5xNyoSOtBRfqJGVj9X3a")));
        mockMvc.perform(get("/user/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("error/403"));
    }

    @DisplayName("перенаправление на login для неаутентифицированных пользователей")
    @Test
    public void testReturn302() throws Exception {
        when(userService.getById(1)).thenReturn(Optional.of(new User(1, "Джоан", "Роулинг", "joan", "$2y$12$161FmGdY386kQZpF1do4i.yMAxcKUBRVl5xNyoSOtBRfqJGVj9X3a")));
        mockMvc.perform(get("/user/1"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("правильно формирует запрос с пользователем при post")
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"}, password = "11111")
    public void postUserCorrectly() throws Exception {
        var user = new User(1, "Джоан", "Роулинг", "joan", "111");
        when(userService.save(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("$2y$12$161FmGdY386kQZpF1do4i.yMAxcKUBRVl5xNyoSOtBRfqJGVj9X3a");
        RequestBuilder request = MockMvcRequestBuilders
                .post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("корректно обрабатывает запрос delete")
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"}, password = "11111")
    public void correctlyHandleDelete() throws Exception {
        mockMvc.perform(delete("/user/{id}", 1)
                .param("id", "1"))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteById(1);
    }

    @DisplayName("возвращение статуса notFound при неуспешном поиске пользователя")
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"}, password = "11111")
    public void testReturnNotFound() throws Exception {
        mockMvc.perform(get("/user/edit/101"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("возвращение статуса notFound при неуспешном поиске пользователя")
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"}, password = "11111")
    public void testReturnBadRequest() throws Exception {
        mockMvc.perform(get("/user/101"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

}