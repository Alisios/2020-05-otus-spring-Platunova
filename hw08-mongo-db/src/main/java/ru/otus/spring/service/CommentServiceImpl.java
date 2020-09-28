package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment save(Comment comment) {
        try {
            return commentRepository.save(comment);
        } catch (RuntimeException ex) {
            throw new DbException("Error with inserting comment " + comment.toString(), ex);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBook(String title, String name, String surname) {
        try {
            commentRepository.deleteAllByBook(title, name, surname);
        } catch (RuntimeException ex) {
            throw new DbException("Error with deleting comment with book title" + title, ex);
        }

    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> getAll() {
        try {
            return commentRepository.findAll();
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding all comments", ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> findByText(String text) {
        try {
            return commentRepository.findAllByTextContainingIgnoreCase(text);
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding comment by text " + text, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> findByBook(Book book) {
        try {
            return commentRepository.findAllByBook_TitleIgnoreCaseAndBook_Author_NameIgnoreCaseAndBook_Author_SurnameIgnoreCase(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname());
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding comment by book with title " + book.getTitle(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void deleteByBookAndText(Book book, String text) {
        try {
            commentRepository.deleteByBook_TitleIgnoreCaseAndBook_Author_NameIgnoreCaseAndBook_Author_SurnameIgnoreCaseAndAndTextContainingIgnoreCase(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname(), text);
        } catch (RuntimeException ex) {
            throw new DbException("Error with deleting comment by book with title " + book.getTitle(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> findCommentsAfterDate(Date date) {
        try {
            return commentRepository.findAllByDateAfter(date);
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding comment by date after " + date, ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> findCommentsBeforeDate(Date date) {
        try {
            return commentRepository.findAllByDateBefore(date);
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding comment by date after " + date, ex);
        }
    }

}
