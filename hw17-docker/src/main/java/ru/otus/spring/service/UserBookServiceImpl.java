package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

@Service
@RequiredArgsConstructor
public class UserBookServiceImpl implements UserBookService {

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookService bookService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Book addBookByUser(Book book) {
        bookService.getById(book.getId()).ifPresent(b -> book.getAuthor().setId(b.getAuthor().getId()));
        Author author = authorService.save(book.getAuthor());
        Genre genre = genreService.save(book.getGenre());
        return bookService.save(new Book(book.getId(), book.getTitle(), author, genre));
    }

}
