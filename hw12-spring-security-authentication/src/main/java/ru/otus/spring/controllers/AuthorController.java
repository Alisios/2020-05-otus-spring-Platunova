package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.spring.dto.AuthorDto.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors/list")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.ok().body(authorService.getAll().stream().map(AuthorDto::toAuthorDto)
                .collect(Collectors.toList()));
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<Long> saveAuthor(@RequestBody AuthorDto author) {
        Author author1 = authorService.save(fromAuthorDto(author));
        return ResponseEntity.ok().body(author1.getId());
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<AuthorDto> showAuthorById(@PathVariable("id") long id) {
        var author = authorService.getById(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok().body(toAuthorDto(author));
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        authorService.deleteById(id);
        return ResponseEntity.ok().body(id.toString());
    }

}
