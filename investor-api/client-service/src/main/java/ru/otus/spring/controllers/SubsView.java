package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.service.dto.SubscriptionDtoFromUser;
import ru.otus.spring.service.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Slf4j
public class SubsView {

    @GetMapping("/sub")
    public String showBook() {
        return "subform";
    }

    @GetMapping("/sub/new")
    public String newBook(Model model) {
        model.addAttribute("book", new SubscriptionDtoFromUser());
        return "subform";
    }

    @GetMapping("/sub/edit/{ticker}/{event}}")
    public String editBook(@PathVariable String ticker, @PathVariable String event, Model model) {
        model.addAttribute("ticker", ticker);
        model.addAttribute("typeEvent", event);
        return "subform";
    }

    @GetMapping("/sub/{ticker}/{event}")
    public String showBookInfo(@PathVariable String ticker, @PathVariable String event, Model model) {
        model.addAttribute("ticker", ticker);
        model.addAttribute("typeEvent", event);
        return "subshow";
    }

    @GetMapping("/subs")
    public String getAllBooks() {
        return "subs";
    }


}
