package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.domain.Author;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorView {

    @GetMapping("/author")
    public String showAuthorByIdView() {
        return "authorform";
    }

    @GetMapping("/author/new")
    public String newAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "authorform";
    }

    @GetMapping("/author/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("authorId", id);
        return "authorform";
    }

    @GetMapping("/authors")
    public String getAllAuthorsView() {
        return "authors";
    }
}
