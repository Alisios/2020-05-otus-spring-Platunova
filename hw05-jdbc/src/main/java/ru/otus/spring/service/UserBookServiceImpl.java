package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

@Service
@RequiredArgsConstructor
public class UserBookServiceImpl implements UserBookService {

    private final DbServiceAuthor dbServiceAuthor;

    private final DbServiceGenre dbServiceGenre;

    private final DbServiceBook dbServiceBook;

    private final IOService ioService;


    @Override
    public Book addBookByUser() {
        ioService.outputMessage("Введите название книги");
        var book = new Book(ioService.inputMessage());
        book.setAuthor(addAuthorByUser());
        book.setGenre(addGenreByUser());
        return dbServiceBook.create(book);
    }

    @Override
    public Genre addGenreByUser() {
        ioService.outputMessage("Введите жанр книги");
        var genre = new Genre(ioService.inputMessage());
        return dbServiceGenre.create(genre);
    }

    @Override
    public Author addAuthorByUser() {
        ioService.outputMessage("Введите имя автора книги");
        var str = ioService.inputMessage();
        ioService.outputMessage("Введите фамилию автора книги");
        var author = new Author(str, ioService.inputMessage());
        return dbServiceAuthor.create(author);
    }
}
