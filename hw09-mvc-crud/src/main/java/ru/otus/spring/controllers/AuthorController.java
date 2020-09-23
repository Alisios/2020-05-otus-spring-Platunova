package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.spring.domain.Author;
import ru.otus.spring.service.AuthorService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    @RequestMapping("/author/new")
    public String newAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "authorform";
    }

    @RequestMapping(value = "/author", method = RequestMethod.POST)
    public String saveAuthor(Author author) {
        authorService.save(author);
        return "redirect:/author/" + author.getId();
    }


    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("authors", authorService.getAll());
        return "authors";
    }

    @RequestMapping("/author/{id}")
    public String showauthorById(@PathVariable("id") long id, Model model) {
        model.addAttribute("author", authorService.getById(id).orElseThrow(NotFoundException::new));
        return "authorshow";
    }

    @RequestMapping("/author/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.getById(id).orElseThrow(NotFoundException::new));
        return "authorform";
    }

    @RequestMapping("/author/delete/{id}")
    public String delete(@PathVariable Long id) {
        authorService.deleteById(id);
        return "redirect:/authors";
    }

}
