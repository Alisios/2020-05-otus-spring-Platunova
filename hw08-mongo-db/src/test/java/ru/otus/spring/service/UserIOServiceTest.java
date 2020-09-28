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
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Тесты проверяют:")
class UserIOServiceTest {

    @Configuration
    @Import(UserIOServiceImpl.class)
    static class NestedConfiguration {
    }

    @MockBean
    private IOService ioService;

    @Autowired
    UserIOService userIOService;

    private final Genre genre = new Genre("фэнтези");
    private final Author author = new Author("Джоан", "Роулинг");
    private final Book book = new Book("Гарри Поттер и Философский камень", author, genre);

    @Test
    @DisplayName("корректно обрабатывает сообщение пользователя и формирует книги")
    void correctlyHandleUserMessageAndCreateBookEntity() {
        when(ioService.inputMessage()).thenReturn("Гарри Поттер и Философский камень").thenReturn("Джоан", "Роулинг");
        assertThat(userIOService.getBookInfoFromUser()).isEqualTo(book);
    }

    @Test
    @DisplayName("корректно обрабатывает сообщение пользователя и формирует сущность автора")
    void correctlyHandleUserMessageAndCreateAuthorEntity() {
        when(ioService.inputMessage()).thenReturn("Джоан", "Роулинг");
        assertThat(userIOService.getAuthorInfoFromUser()).isEqualTo(author);
    }

}