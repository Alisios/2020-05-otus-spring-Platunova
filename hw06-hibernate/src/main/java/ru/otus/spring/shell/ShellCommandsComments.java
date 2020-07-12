
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
public class ShellCommandsComments {

    private final DbServiceComment dbServiceComment;

    private final IOService ioService;

    private final UserBookService userBookService;

    @ShellMethod(value = "create comment", key = {"comment", "create-comment"})
    public void createComment() {
        try {
            userBookService.addCommentByUser();
            ioService.outputMessage("Комментарий добавлен");
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при создании комментария");
        }
    }

    @ShellMethod(value = "show comments", key = {"showC", "show-all-comments"})
    public void showAllComments() {
        try {
            dbServiceComment.getAll().forEach((book) -> ioService.outputMessage(book.toString()));
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе комментария");
        }
    }

    @ShellMethod(value = "show comment by book id", key = {"showCbyBId", "show-comment-book-id"})
    public void showCommentById() {
        try {
            ioService.outputMessage("Введите id книги, на которую нужны комментарии");
            long id = Integer.parseInt(ioService.inputMessage());
            dbServiceComment.findByBookId(id).forEach((comment) -> ioService.outputMessage(comment.toStringWithoutBook()));
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при поиске комментария с id ");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе комментария. Обратите внимание, что Id должно быть числом");
        }
    }

    @ShellMethod(value = "show comment by text", key = {"showCbyTxt", "show-comment-text"})
    public void showCommentByText() {
        try {
            ioService.outputMessage("Введите текст комментария");
            dbServiceComment.findByText(ioService.inputMessage()).forEach((comment) -> ioService.outputMessage(comment.toString()));
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при поиске комментария с id ");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе комментария");
        }
    }

    @ShellMethod(value = "delete comment by id command", key = {"delC", "delete-comment-id"})
    public void deleteCommentById() {
        try {
            ioService.outputMessage("Введите id комментария для удаления");
            long id = Integer.parseInt(ioService.inputMessage());
            dbServiceComment.deleteById(id);
            ioService.outputMessage("Комментарий c id " + id + " удален");
        } catch (DaoException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при удалении комментария");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе комментария. Обратите внимание, что Id должно быть числом");
        }
    }

}