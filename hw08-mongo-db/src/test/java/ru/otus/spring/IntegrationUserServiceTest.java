package ru.otus.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@IntegrationTestAnnotation
public class IntegrationUserServiceTest {

    @Configuration
    @Import({UserBookServiceImpl.class, AuthorServiceImpl.class, BookServiceImpl.class, UserIOServiceImpl.class, CommentServiceImpl.class})
    static class NestedConfiguration {
    }

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CommentService commentService;

    @MockBean
    private IOService ioService;

    @Autowired
    private UserBookService userBookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserIOService userIOService;

    @Test
    @DisplayName("корректное сохранение книги и автора по информации от пользователя")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlySaveBookANdAuthorByUser() {
        long countA = authorRepository.count();
        long countB = bookRepository.count();
        Book book = new Book("Маленький Принц", new Author("Антуан", "де Сант-Экзюпери"), new Genre("сказка"));
        when(ioService.inputMessage()).thenReturn(book.getTitle())
                .thenReturn(book.getAuthor().getName())
                .thenReturn(book.getAuthor().getSurname())
                .thenReturn("фэнтези");
        userBookService.addBookByUser();
        assertThat(authorRepository.findAll().get(0).getListOfBookTitles()).isNotEmpty().hasSize(1);
        assertThat(bookRepository.count()).isEqualTo(countB + 1);
        assertThat(authorRepository.count()).isEqualTo(countA + 1);
    }

    @Test
    @DisplayName("корректное сохранение комментария по информации от пользователя")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlySaveCommentByUser() {
        long countC = commentRepository.count();
        var author = new Author("Антуан", "де Сант-Экзюпери");
        var book = new Book("Маленький Принц", author, new Genre("сказка"));
        author.getListOfBookTitles().add(book.getTitle());
        authorService.save(author);
        bookService.save(book);

        when(ioService.inputMessage()).thenReturn(book.getTitle())
                .thenReturn(book.getAuthor().getName())
                .thenReturn(book.getAuthor().getSurname())
                .thenReturn("Супер!");
        userBookService.addCommentByUser();
        assertThat(commentRepository.count()).isEqualTo(countC + 1);
        assertThat(commentRepository.findAllByTextContainingIgnoreCase("Супер!")).isNotEmpty();
    }

//    @Test
//    @DisplayName("откатывает всю транзакцию если на какой-т остадии что-то идет не так")
//    void rollbackWholeTransactionIfExceptionWasThrownInAnyStep() {
//        long countA = authorRepository.count();
//        long countB = bookRepository.count();
//        Book book = new Book("Маленький Принц", new Author("Антуан","де Сант-Экзюпери"),new Genre("сказка"));
//        when(ioService.inputMessage()).thenReturn(book.getTitle())
//                .thenReturn(book.getAuthor().getName())
//                .thenReturn(book.getAuthor().getSurname())
//                .thenThrow(DbException.class);
//        assertThrows(DbException.class, ()->userBookService.addBookByUser());
//        assertThat(bookRepository.count()).isEqualTo(countB);
//        assertThat(authorRepository.count()).isEqualTo(countA);
//    }

}