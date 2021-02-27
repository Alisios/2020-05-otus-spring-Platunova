package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.UserBookService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.spring.dto.BookDto.fromBookDto;
import static ru.otus.spring.dto.BookDto.toBookDto;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;
    private final UserBookService userBookService;

    @PostMapping(path = "/books")
    public ResponseEntity<Long> saveBook(@RequestBody BookDto bookDto) {
        Book book =  userBookService.addBookByUser(fromBookDto(bookDto));
        return ResponseEntity.ok().body(book.getId());
    }

    @GetMapping("/books/list")
    public ResponseEntity<List<BookDto>> list() {
        return ResponseEntity.ok().body(bookService.getAll().stream().map(BookDto::toBookDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/book/info/{id}")
    public ResponseEntity<BookDto> showBookById(@PathVariable("id") long id) {
        var book = bookService.getById(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok().body(toBookDto(book));
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().body(id.toString());
    }

}
