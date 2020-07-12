
package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.dao.DaoException;
import ru.otus.spring.service.*;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class ShellCommandsGenre {

    private final DbServiceGenre dbServiceGenre;

    private final IOService ioService;

    private final UserBookService userBookService;

    @ShellMethod(value = "create genre", key = {"createG", "create-genre"})
    public void createGenre() {
        try {
            userBookService.addGenreByUser();
            ioService.outputMessage("Жанр добавлен");
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при создании жанра");
        }
    }

    @ShellMethod(value = "show genres", key = {"showG", "show-all-genres"})
    public void showAllGenres() {
        try {
            dbServiceGenre.getAll().forEach((book) -> ioService.outputMessage(book.toString()));
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе жанра");
        }
    }

    @ShellMethod(value = "show genre by id", key = {"showIdG", "show-genre-id"})
    public void showGenreById() {
        try {
            ioService.outputMessage("Введите id жанра");
            long id = Integer.parseInt(ioService.inputMessage());
            dbServiceGenre.getById(id).ifPresent((book) -> ioService.outputMessage(book.toString()));
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при поиске жанра с id ");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе жанра. Обратите внимание, что Id должно быть числом");
        }
    }

    @ShellMethod(value = "delete genre by id command", key = {"delG", "delete-genre-id"})
    public void deleteGenreById() {
        try {
            ioService.outputMessage("Введите id жанра для удаления");
            long id = Integer.parseInt(ioService.inputMessage());
            dbServiceGenre.deleteById(id);
            ioService.outputMessage("Жанр c id " + id + " удален");
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при удалении жанра");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе жанра. Обратите внимание, что Id должно быть числом");
        }
    }
}