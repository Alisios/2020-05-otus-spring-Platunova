package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.DbException;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBookServiceImpl implements UserBookService {

    private final DbServiceAuthor dbServiceAuthor;

    private final DbServiceGenre dbServiceGenre;

    private final DbServiceBook dbServiceBook;

    private final DbServiceComment dbServiceComment;

    private final IOService ioService;


    @Override
    public Book addBookByUser() {
        ioService.outputMessage("Введите название книги");
        var book = new Book(ioService.inputMessage());
        book.setAuthor(addAuthorByUser());
        book.setGenre(addGenreByUser());
        return dbServiceBook.save(book);
    }

    @Override
    public Genre addGenreByUser() {
        ioService.outputMessage("Введите жанр книги");
        var genre = new Genre(ioService.inputMessage());
        return dbServiceGenre.save(genre);
    }

    @Override
    public Author addAuthorByUser() {
        ioService.outputMessage("Введите имя автора книги");
        var str = ioService.inputMessage();
        ioService.outputMessage("Введите фамилию автора книги");
        var author = new Author(str, ioService.inputMessage());
        return dbServiceAuthor.save(author);
    }

    @Override
    public Comment addCommentByUser() {
        ioService.outputMessage("введите id книги, которую хотите прокомментировать: ");
        long id = Long.parseLong(ioService.inputMessage());
        ioService.outputMessage("Введите текст комментария:");
        try {
            var comment = new Comment(dbServiceBook.getById(id).orElseThrow(() -> new DbException("Нет книги с данным id")), ioService.inputMessage());
            return dbServiceComment.save(comment);
        } catch (DbException ex) {
            throw new DbException("Ошибка при поиске книги. Убедитесь, что книга с таким id сущствует", ex);
        }
    }

    @Override
    public String printBooksWithComments() {
        ioService.outputMessage("введите id книги, по которой вы хотите посмотреть комментарии: ");
        long id = Long.parseLong(ioService.inputMessage());
        Book book = dbServiceBook.getById(id).orElseThrow(() -> new DbException("Нет книги с данным id"));
        List<Comment> comments = dbServiceComment.findByBookId(id);
        return book.toString() + ". \n" + comments.stream().map(Comment::toStringWithoutBook).collect(Collectors.joining(";\n"));
    }
}
