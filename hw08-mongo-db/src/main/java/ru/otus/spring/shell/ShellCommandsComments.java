
package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.service.*;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class ShellCommandsComments {

    private final CommentService commentService;

    private final IOService ioService;

    private final UserBookService userBookService;

    private final UserIOService userIOService;

    @ShellMethod(value = "create comment", key = {"comment", "create-comment"})
    public void createComment() {
        try {
            userBookService.addCommentByUser();
            ioService.outputMessage("Комментарий добавлен");
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при создании комментария");
        }
    }

    @ShellMethod(value = "show comments", key = {"showC", "show-all-comments"})
    public void showAllComments() {
        try {
            commentService.getAll().forEach((book) -> ioService.outputMessage(book.toString()));
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе комментария");
        }
    }

    @ShellMethod(value = "show comment by book", key = {"showCbyBook", "show-comment-book"})
    public void showCommentByBook() {
        try {
            var book = userIOService.getBookInfoFromUser();
            commentService.findByBook(book).forEach((comment) -> ioService.outputMessage(comment.toStringWithoutBook()));
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при поиске комментария по книге ");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе комментария. Обратите внимание на ввод");
        }
    }

    @ShellMethod(value = "show comment by text", key = {"showCbyTxt", "show-comment-text"})
    public void showCommentByText() {
        try {
            ioService.outputMessage("Введите текст комментария");
            commentService.findByText(ioService.inputMessage()).forEach((comment) -> ioService.outputMessage(comment.toString()));
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при поиске комментария с id ");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе комментария");
        }
    }

    @ShellMethod(value = "delete all comments by book ", key = {"delC", "delete-comment-book"})
    public void deleteCommentsByBook() {
        try {
            var book = userIOService.getBookInfoFromUser();
            commentService.deleteByBook(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname());
            ioService.outputMessage("Комментарии для книги удалены");
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при удалении комментариев");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе комментария. Обратите внимание на ввод");
        }
    }

    @ShellMethod(value = "delete all comments by book and text", key = {"delCT", "delete-comment-book-text"})
    public void deleteCommentsByBookAndText() {
        try {
            var book = userIOService.getBookInfoFromUser();
            ioService.outputMessage("Введите текст комментария");
            commentService.deleteByBookAndText(book, ioService.inputMessage());
            ioService.outputMessage("Комментарии удалены");
        } catch (DbException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при удалении комментариев");
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex.getCause());
            ioService.outputMessage("Ошибка при выводе комментария. Обратите внимание на ввод");
        }
    }

}