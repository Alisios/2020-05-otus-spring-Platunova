package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.domain.Book;


@Controller
@RequiredArgsConstructor
@Slf4j
public class BookWithCommentsView {

    @GetMapping("/book")
    public String showBook() {
        return "bookform";
    }

    @GetMapping("/book/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "bookform";
    }

    @GetMapping("/book/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        model.addAttribute("bookId", id);
        return "bookform";
    }

    @GetMapping("/book/{id}")
    public String showBookInfo(@PathVariable Long id, Model model) {
        model.addAttribute("bookId", id);
        return "bookcommentshow";
    }

    @GetMapping("/books")
    public String getAllBooks() {
        return "books";
    }
}
