package ru.otus.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.repository.ServiceException;
import ru.otus.spring.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@IntegrationTest
public class IntegrationUserServiceTest {

    @Configuration
    @Import({UserBookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, BookServiceImpl.class})
    static class NestedConfiguration {
    }

    @Autowired
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Autowired
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private UserBookService userBookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;


    @Test
    @DisplayName("откатывает всю транзакцию если на какой-т остадии что-то идет не так")
    void rollbackWholeTransactionIfExceptionWasThrownInAnyStep() {
        long countA = authorRepository.count();
        long countB = bookRepository.count();
        long countG = genreRepository.count();
        Genre genre = new Genre("фэнтези");
        Book book = new Book("Цветы для Эрженона", new Author("Дэниела", "Киз"), genre);
        when(genreService.save(genre)).thenThrow(ServiceException.class);
        assertThrows(ServiceException.class, () -> userBookService.addBookByUser(book));
        assertThat(bookRepository.count()).isEqualTo(countB);
        assertThat(genreRepository.count()).isEqualTo(countG);
        assertThat(authorRepository.count()).isEqualTo(countA);
    }

}
