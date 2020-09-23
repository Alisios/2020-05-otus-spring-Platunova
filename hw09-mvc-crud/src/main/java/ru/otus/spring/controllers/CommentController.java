package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.Date;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final BookService bookService;
    private final CommentService commentService;

    @RequestMapping("/book/{id}/comments")
    public String showBookWithComments(@PathVariable("id") long id, Model model) {
        var book = bookService.getById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", commentService.findByBookId(book.getId()));
        return "bookcommentshow";
    }

    @RequestMapping(value = "/book/{id}/comment", method = RequestMethod.POST)
    public String saveComment(@PathVariable("id") long id, Comment comment) {
        var book = bookService.getById(id).orElseThrow(NotFoundException::new);
        comment.setBook(book);
        comment.setDate(new Date());
        commentService.save(comment);
        return "redirect:/book/" + id + "/comments";
    }

    @RequestMapping(value = "/book/{bookId}/comment/delete/{id}")
    public String deleteComment(@PathVariable("bookId") long bookId, @PathVariable("id") long id) {
        commentService.deleteById(id);
        return "redirect:/book/" + bookId + "/comments";
    }

}
