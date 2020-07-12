package ru.otus.spring.shell;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.dao.DaoException;
import ru.otus.spring.service.DbServiceBook;
import ru.otus.spring.service.IOService;
import ru.otus.spring.service.UserBookService;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class ShellCommands {

    private final DbServiceBook dbServiceBook;

    private final IOService ioService;

    private final UserBookService userBookService;

    @ShellMethod(value = "create book", key = {"create", "create-book"})
    public void createBook() {
        try {
            userBookService.addBookByUser();
            ioService.outputMessage("Книга добавлена");
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при создании книги");
        }
    }

    @ShellMethod(value = "show books", key = {"showB", "show-all-books"})
    public void showAllBooks() {
        try {
            dbServiceBook.getAll().forEach((book) -> ioService.outputMessage(book.toString()));
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книг");
        }
    }

    @ShellMethod(value = "show books", key = {"showBC", "show-all-books-comments"})
    public void showAllBooksWithComments() {
        try {
            ioService.outputMessage(userBookService.printBooksWithComments());
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книг c комментариями");
        }
    }

    @ShellMethod(value = "show book by id", key = {"showId", "show-book-id"})
    public void showBookById() {
        try {
            ioService.outputMessage("Введите id книги");
            long id = Integer.parseInt(ioService.inputMessage());
            dbServiceBook.getById(id).ifPresent((book) -> ioService.outputMessage(book.toString()));
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при поиске книги с id ");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книги. Обратите внимание, что Id должно быть числом");
        }
    }

    @ShellMethod(value = "delete book by id command", key = {"del", "delete-book-id"})
    public void deleteBookById() {
        try {
            ioService.outputMessage("Введите id книги");
            long id = Integer.parseInt(ioService.inputMessage());
            dbServiceBook.deleteById(id);
            ioService.outputMessage("Книга c id " + id + " удалена");
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при удалении книги");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книги. Обратите внимание, что Id должно быть числом");
        }
    }

}