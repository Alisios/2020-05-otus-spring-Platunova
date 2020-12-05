package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;

@Service
@RequiredArgsConstructor
public class UserBookServiceImpl implements UserBookService {

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookService bookService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book addBookByUser(Book book) {
        bookService.getById(book.getId()).ifPresent(b -> book.getAuthor().setId(b.getAuthor().getId()));
        return bookService.save(new Book(book.getId(), book.getTitle(), authorService.save(book.getAuthor()), genreService.save(book.getGenre())));
    }

}
