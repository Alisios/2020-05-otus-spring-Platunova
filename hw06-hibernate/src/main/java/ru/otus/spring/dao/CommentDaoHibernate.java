package ru.otus.spring.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Transactional
public class CommentDaoHibernate implements CommentDao {

    @PersistenceContext
    final private EntityManager em;

    @Override
    public Comment insert(Comment comment) {
        if (comment.getId() <= 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public void update(Comment comment) {
        Query query = em.createQuery("update Comment g set g.text = :text where g.id = :id");
        query.setParameter("text", comment.getText());
        query.setParameter("id", comment.getId());
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Comment g where g.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteByBookId(long id) {
        Query query = em.createQuery("delete from Comment g where g.book.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Comment> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-comment-entity-graph");
        TypedQuery<Comment> query = em.createQuery("select b FROM Comment b", Comment.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public long count() {
        return em.createQuery("select count(b) from Comment b", Long.class).getSingleResult();
    }


    @Override
    public List<Comment> findByText(String text) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-comment-entity-graph");
        TypedQuery<Comment> query = em.createQuery("select c from Comment c WHERE c.text LIKE :text", Comment.class);
        query.setParameter("text", "%" + text + "%");
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public List<Comment> findByBook(Book book) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-comment-entity-graph");
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.book.title = :title and c.book.author.name = :name and c.book.author.surname = :surname", Comment.class);
        query.setParameter("title", book.getTitle());
        query.setParameter("name", book.getAuthor().getName());
        query.setParameter("surname", book.getAuthor().getSurname());
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public List<Comment> findByBookId(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-comment-entity-graph");
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.book.id = :id", Comment.class);
        query.setParameter("id", id);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }
}
