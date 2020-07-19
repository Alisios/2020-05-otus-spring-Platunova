package ru.otus.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.DbException;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Интеграционный тест проверяет:")
@EnableJpaRepositories(basePackages = "ru.otus.spring.repository")
@EntityScan("ru.otus.spring.domain")
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class IntegrationUserServiceTest {

    @Configuration
    @Import({UserBookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, BookServiceImpl.class})
    static class NestedConfiguration {}

    @Autowired
    private  AuthorService authorService;

    @Autowired
    private  GenreService genreService;

    @Autowired
    private  BookService bookService;

    @MockBean
    private  CommentService commentService;

    @MockBean
    private  IOService ioService;

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
        Book book = new Book("Цветы для Эрженона", new Author("Дэниела","Киз"),null);
        when(ioService.inputMessage()).thenReturn(book.getTitle())
                .thenReturn(book.getAuthor().getName())
                .thenReturn(book.getAuthor().getSurname())
                .thenThrow(DbException.class);
        assertThrows(DbException.class, ()->userBookService.addBookByUser());
        assertThat(bookRepository.count()).isEqualTo(countB);
        assertThat(genreRepository.count()).isEqualTo(countG);
        assertThat(authorRepository.count()).isEqualTo(countA);
    }

}
