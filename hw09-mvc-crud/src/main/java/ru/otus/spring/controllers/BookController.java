package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Book;
import ru.otus.spring.service.BookService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping("/book/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "bookform";
    }

    @PostMapping("/book")
    public String saveBook(Book book) {
        bookService.save(book);
        return "redirect:/book/" + book.getId();
    }


    @GetMapping("/books")
    public String list(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "books";
    }

    @GetMapping("/book/{id}")
    public String showBookById(@PathVariable("id") long id, Model model) {
        var book = bookService.getById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "bookshow";
    }

    @GetMapping("/book/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var book = bookService.getById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "bookform";
    }

    @GetMapping("/book/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

}
