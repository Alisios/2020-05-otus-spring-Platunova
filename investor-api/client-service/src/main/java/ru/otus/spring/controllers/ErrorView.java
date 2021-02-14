package ru.otus.spring.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ErrorView {

    @GetMapping("/error/403")
    public String show403() {
        return "error/403";
    }
}
