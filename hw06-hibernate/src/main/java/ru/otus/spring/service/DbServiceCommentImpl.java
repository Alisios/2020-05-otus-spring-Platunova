package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.CommentDao;
import ru.otus.spring.dao.DaoException;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DbServiceCommentImpl implements DbServiceComment {

    private final CommentDao commentDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment create(Comment comment) {
        try {
            return commentDao.insert(comment);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with inserting comment " + comment.toString(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Comment comment) {
        try {
            commentDao.update(comment);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with updating comment " + comment.toString(), ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        try {
            commentDao.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with deleting comment  with id" + id, ex);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBookId(long id) {
        try {
            commentDao.deleteByBookId(id);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with deleting comment with book id" + id, ex);
        }

    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> getAll() {
        try {
            return commentDao.findAll();
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding all comments", ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> findByText(String text) {
        try {
            return commentDao.findByText(text);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding comment by text " + text, ex);
        }

    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> findByBook(Book book) {
        try {
            return commentDao.findByBook(book);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding comment by book with title " + book.getTitle(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Comment> findByBookId(long id) {
        try {
            return commentDao.findByBookId(id);
        } catch (RuntimeException ex) {
            throw new DaoException("Error with finding comment by book with id " + id, ex);
        }
    }

}
