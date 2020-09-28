package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Author;
import ru.otus.spring.service.AuthorService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author/new")
    public String newAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "authorform";
    }

    @PostMapping("/author")
    public String saveAuthor(Author author) {
        authorService.save(author);
        return "redirect:/author/" + author.getId();
    }

    @GetMapping("/authors")
    public String list(Model model) {
        model.addAttribute("authors", authorService.getAll());
        return "authors";
    }

    @GetMapping("/author/{id}")
    public String showauthorById(@PathVariable("id") long id, Model model) {
        var author = authorService.getById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("author", author);
        return "authorshow";
    }

    @GetMapping("/author/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var author = authorService.getById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("author", author);
        return "authorform";
    }

    @GetMapping("/author/delete/{id}")
    public String delete(@PathVariable Long id) {
        authorService.deleteById(id);
        return "redirect:/authors";
    }

}
