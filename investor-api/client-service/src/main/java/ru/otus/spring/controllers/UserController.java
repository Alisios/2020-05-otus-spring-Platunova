package ru.otus.spring.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.service.UserService;
import ru.otus.spring.service.dto.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(path = "/users")
    public ResponseEntity<Long> saveUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok().body(userService.save(user).getId());
    }

    @GetMapping("/users/list")
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.ok().body(userService.getAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> showAuthorById(@PathVariable("id") long id) {
        var user = userService.getById(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok().body(user);
    }
//
//    @DeleteMapping("/user/{id}")
//    public ResponseEntity<String> delete(@PathVariable Long id) {
//        userService.deleteById(id);
//        return ResponseEntity.ok().body(id.toString());
//    }
}
