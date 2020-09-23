package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.spring.domain.Book;
import ru.otus.spring.service.BookService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @RequestMapping("/book/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "bookform";
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public String saveBook(Book book) {
        bookService.save(book);
        return "redirect:/book/" + book.getId();
    }


    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "books";
    }

    @RequestMapping("/book/{id}")
    public String showBookById(@PathVariable("id") long id, Model model) {
        model.addAttribute("book", bookService.getById(id).orElseThrow(NotFoundException::new));
        return "bookshow";
    }

    @RequestMapping("/book/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getById(id).orElseThrow(NotFoundException::new));
        return "bookform";
    }

    @RequestMapping("/book/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

}
