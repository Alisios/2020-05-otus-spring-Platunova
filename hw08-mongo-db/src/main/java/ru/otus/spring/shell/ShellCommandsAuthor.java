package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.domain.Author;
import ru.otus.spring.service.*;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class ShellCommandsAuthor {

    private final AuthorService authorService;

    private final IOService ioService;

    private final UserBookService userBookService;

    private final UserIOService userIOService;

    @ShellMethod(value = "create author", key = {"createA", "create-author"})
    public void createAuthor() {
        try {
            userBookService.addAuthorByUser();
            ioService.outputMessage("автор добавлен");
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при создании автора");
        }
    }

    @ShellMethod(value = "show authors", key = {"showA", "show-all-authors"})
    public void showAllAuthors() {
        try {
            authorService.getAll().forEach((book) -> ioService.outputMessage(book.toString()));
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе автора");
        }
    }

    @ShellMethod(value = "show books by author", key = {"showBbyA", "show-books-by-author"})
    public void showAllBooksByAuthor() {
        try {
            Author author = userIOService.getAuthorInfoFromUser();
            authorService.getBookTitlesByAuthor(author.getName(), author.getSurname()).forEach((book) -> ioService.outputMessage(book.toString()));
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе автора. Возможно, такого автора не существует");
        }
    }


    @ShellMethod(value = "delete author by name and surname", key = {"delAName", "delete-author-name"})
    public void deleteAuthorByName() {
        try {
            Author author = userIOService.getAuthorInfoFromUser();
            authorService.deleteByNameAndSurname(author.getName(), author.getSurname());
            ioService.outputMessage("автор " + author.getName() + " " + author.getSurname() + " удален");
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при удалении автора");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе автора.");
        }
    }

}