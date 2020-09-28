package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

@Service
@RequiredArgsConstructor
public class UserIOServiceImpl implements UserIOService {

    private final IOService ioService;

    @Override
    public Book getBookInfoFromUser() {
        var book = new Book();
        ioService.outputMessage("Введите название книги");
        book.setTitle(ioService.inputMessage());
        book.setAuthor(getAuthorInfoFromUser());
        return book;
    }

    public Author getAuthorInfoFromUser() {
        var author = new Author();
        ioService.outputMessage("Введите имя автора");
        author.setName(ioService.inputMessage());
        ioService.outputMessage("Введите фамилию автора");
        author.setSurname(ioService.inputMessage());
        return author;
    }
}
