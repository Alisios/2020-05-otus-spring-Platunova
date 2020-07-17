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
public class UserBookServiceImpl implements UserBookService {

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookService bookService;

    private final CommentService commentService;

    private final IOService ioService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book addBookByUser() {
        ioService.outputMessage("Введите название книги");
        var book = new Book(ioService.inputMessage());
        book.setAuthor(addAuthorByUser());
        book.setGenre(addGenreByUser());
        return bookService.save(book);
    }

    @Override
    public Genre addGenreByUser() {
        ioService.outputMessage("Введите жанр книги");
        var genre = new Genre(ioService.inputMessage());
        return genreService.save(genre);
    }

    @Override
    public Author addAuthorByUser() {
        ioService.outputMessage("Введите имя автора книги");
        var str = ioService.inputMessage();
        ioService.outputMessage("Введите фамилию автора книги");
        var author = new Author(str, ioService.inputMessage());
        return authorService.save(author);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment addCommentByUser() {
        ioService.outputMessage("введите id книги, которую хотите прокомментировать: ");
        long id = Long.parseLong(ioService.inputMessage());
        ioService.outputMessage("Введите текст комментария:");
        try {
            var comment = new Comment(bookService.getById(id).orElseThrow(() -> new DbException("Нет книги с данным id")), ioService.inputMessage());
            return commentService.save(comment);
        } catch (DbException ex) {
            throw new DbException("Ошибка при поиске книги. Убедитесь, что книга с таким id сущствует", ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public String printBooksWithComments() {
        ioService.outputMessage("введите id книги, по которой вы хотите посмотреть комментарии: ");
        long id = Long.parseLong(ioService.inputMessage());
        Book book = bookService.getById(id).orElseThrow(() -> new DbException("Нет книги с данным id"));
        List<Comment> comments = commentService.findByBookId(id);
        return book.toString() + ". \n" + comments.stream().map(Comment::toStringWithoutBook).collect(Collectors.joining(";\n"));
    }
}
