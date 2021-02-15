package ru.otus.spring.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.service.dto.User;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserView {

    @GetMapping("/users")
    public String getAllUsersView() {
        return "users";
    }

    @GetMapping("/user/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "userform";
    }

    @GetMapping("/user")
    public String showUserByIdView() {
        return "userform";
    }

    @GetMapping("/user/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "userform";
    }


}
