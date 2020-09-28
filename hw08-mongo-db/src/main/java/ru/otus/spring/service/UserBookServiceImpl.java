package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBookServiceImpl implements UserBookService {

    private final AuthorService authorService;

    private final BookService bookService;

    private final CommentService commentService;

    private final IOService ioService;

    private final UserIOService userIOService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book addBookByUser() {
        ioService.outputMessage("Введите название книги");
        String title = ioService.inputMessage();
        var book = new Book(title);
        var author = addAuthorWithTitleByUser(title);
        book.setAuthor(author);
        ioService.outputMessage("Введите жанр книги");
        book.setGenre(new Genre(ioService.inputMessage()));
        book = bookService.save(book);
        return book;
    }


    @Override
    public Author addAuthorByUser() {
        return authorService.save(userIOService.getAuthorInfoFromUser());
    }

    @Override
    public Author addAuthorWithTitleByUser(String title) {
        var author = userIOService.getAuthorInfoFromUser();
        author.getListOfBookTitles().add(title);
        return authorService.save(author);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment addCommentByUser() {
        var book = userIOService.getBookInfoFromUser();
        ioService.outputMessage("Введите текст комментария:");
        try {
            var comment = new Comment(bookService.getByTitleAndAuthor(book)
                    .orElseThrow(() -> new DbException("Нет книги с таким названием и автором")),
                    ioService.inputMessage(),
                    new Date());
            return commentService.save(comment);
        } catch (DbException ex) {
            throw new DbException("Ошибка при поиске книги. Убедитесь, что книга с таким id сущствует", ex);
        }
    }

    @Override
    public String printBooksWithComments() {
        Book book = bookService.getByTitleAndAuthor(userIOService.getBookInfoFromUser()).orElseThrow(() -> new DbException("Книга с данным названием не найдена"));
        List<Comment> comments = commentService.findByBook(book);
        return book.toString() + ". \n" +
                (comments.isEmpty()
                        ? "Комментариев к данной книге пока нет!"
                        : comments.stream().map(Comment::toStringWithoutBook).collect(Collectors.joining(";\n")));
    }
}
