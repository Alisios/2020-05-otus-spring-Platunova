package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.dao.DbException;
import ru.otus.spring.service.*;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class ShellCommandsAuthor {

    private final AuthorService authorService;

    private final IOService ioService;

    private final UserBookService userBookService;

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

    @ShellMethod(value = "show author by id", key = {"showIdA", "show-author-id"})
    public void showAuthorById() {
        try {
            ioService.outputMessage("Введите id автора");
            long id = Integer.parseInt(ioService.inputMessage());
            authorService.getById(id).ifPresent((book) -> ioService.outputMessage(book.toString()));
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при поиске автора с id ");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе автора. Обратите внимание, что Id должно быть числом");
        }
    }

    @ShellMethod(value = "delete author by id command", key = {"delA", "delete-author-id"})
    public void deleteAuthorById() {
        try {
            ioService.outputMessage("Введите id автора для удаления");
            long id = Integer.parseInt(ioService.inputMessage());
            authorService.deleteById(id);
            ioService.outputMessage("автор c id " + id + " удалена");
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при удалении автора");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе автора. Обратите внимание, что Id должно быть числом");
        }
    }
}