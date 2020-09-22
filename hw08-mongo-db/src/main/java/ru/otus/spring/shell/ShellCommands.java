package ru.otus.spring.shell;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.service.*;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class ShellCommands {

    private final BookService bookService;

    private final IOService ioService;

    private final UserIOService userIOService;

    private final UserBookService userBookService;

    @ShellMethod(value = "create book", key = {"create", "create-book"})
    public void createBook() {
        try {
            userBookService.addBookByUser();
            ioService.outputMessage("Книга добавлена");
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при создании книги");
        }
    }

    @ShellMethod(value = "show books", key = {"showB", "show-all-books"})
    public void showAllBooks() {
        try {
            bookService.getAll().forEach((book) -> ioService.outputMessage(book.toString()));
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книг");
        }
    }

    @ShellMethod(value = "show book with comment", key = {"showBC", "show-book-with-comments"})
    public void showAllBooksWithComments() {
        try {
            ioService.outputMessage(userBookService.printBooksWithComments());
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книг c комментариями");
        }
    }

    @ShellMethod(value = "show book by title", key = {"showTitle", "show-book-title"})
    public void showBookByTitle() {
        try {
            ioService.outputMessage("Введите название книги");
            String title = ioService.inputMessage();
            List<Book> books = bookService.getByTitle(title);
            if (books.isEmpty())
                ioService.outputMessage("Книги с таким названием не существует. Вы можете добавить ее при помощи команды create\n");
            else if (books.size() == 1)
                ioService.outputMessage(books.get(0).toString());
            else {
                ioService.outputMessage("Найдены несколько книг с таким названием. Вы можете уточнить запрос если введете название книги и автора при помощи команды showTitleAuthor\n");
                bookService.getByTitle(title).forEach((book) -> ioService.outputMessage(book.toString()));
            }
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при поиске книги с введенным названием");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книги");
        }
    }

    @ShellMethod(value = "show book by title and author", key = {"showTitleAuthor", "show-book-title-author"})
    public void showBookByTitleAuthor() {
        try {
            Book book = userIOService.getBookInfoFromUser();
            bookService.getByTitleAndAuthor(book).ifPresent((b) -> ioService.outputMessage(b.toString()));
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при поиске книги с введенным названием и атвором");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книги");
        }
    }

    @ShellMethod(value = "delete book by title and author", key = {"del", "delete-book-title-author"})
    public void deleteBookByTitleAndAuthor() {
        try {
            Book book = userIOService.getBookInfoFromUser();
            bookService.deleteByTitleAndAuthor(book);
            ioService.outputMessage("Книга \" " + book.getTitle() + "\" автора " + book.getAuthor().getName() + " " + book.getAuthor().getSurname() + " удалена");
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при удалении книги");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книги.");
        }
    }

    @ShellMethod(value = "delete book by author", key = {"delBByA", "delete-book-author"})
    public void deleteBookByAuthor() {
        try {
            Author author = userIOService.getAuthorInfoFromUser();
            bookService.deleteBookByAuthor(author.getName(), author.getSurname());
            ioService.outputMessage("Книга автора " + author.getName() + " " + author.getSurname() + " удалена");
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при удалении книги");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе книги.");
        }
    }

}