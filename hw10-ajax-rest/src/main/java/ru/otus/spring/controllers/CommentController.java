package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.spring.dto.CommentDto.toCommentDto;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final BookService bookService;
    private final CommentService commentService;

    @GetMapping("/book/{id}/comments/list")
    public ResponseEntity<List<CommentDto>> showBookWithComments(@PathVariable("id") long id, Model model) {
        return ResponseEntity.ok().body(commentService.findByBookId(id).stream().map(CommentDto::toCommentDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/book/{id}/comments")
    public ResponseEntity<CommentDto> saveComment(@PathVariable("id") long id, @RequestBody CommentDto commentDto) {
        var book = bookService.getById(id).orElseThrow(NotFoundException::new);
        Comment comment = commentService.save(new Comment(book, commentDto.getText(), new Date()));
        return ResponseEntity.ok().body(toCommentDto(comment));
    }

    @DeleteMapping(value = "/book/{bookId}/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("bookId") long bookId, @PathVariable("id") long commentId) {
        commentService.deleteById(commentId);
        return ResponseEntity.ok().body(String.valueOf(commentId));
    }

}
