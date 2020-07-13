package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.DbException;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DbServiceCommentImpl implements DbServiceComment {

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
    public void deleteById(long id) {
        try {
            commentRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DbException("Error with deleting comment  with id" + id, ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBookId(long id) {
        try {
            commentRepository.deleteByBookId(id);
        } catch (RuntimeException ex) {
            throw new DbException("Error with deleting comment with book id" + id, ex);
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
            return commentRepository.findByTextContainingIgnoreCase(text);
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding comment by text " + text, ex);
        }

    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> findByBook(Book book) {
        try {
            return commentRepository.findByBook(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname());
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding comment by book with title " + book.getTitle(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> findByBookId(long id) {
        try {
            return commentRepository.findByBookId(id);
        } catch (RuntimeException ex) {
            throw new DbException("Error with finding comment by book with id " + id, ex);
        }
    }

}
