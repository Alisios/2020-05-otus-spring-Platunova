package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookDaoHibernate implements BookDao {

    @PersistenceContext
    final private EntityManager em;

    @Override
    public Book insert(Book book) {
        if (book.getId() <= 0) {
            em.persist(book);
            //   em.flush();
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public void updateByTitle(Book book) {
        Query query = em.createQuery("update Book b set b.title = :title where b.id = :id");
        query.setParameter("title", book.getTitle());
        query.setParameter("id", book.getId());
        query.executeUpdate();
    }

    @Override
    public long count() {
        return em.createQuery("select count(b) from Book b", Long.class).getSingleResult();
    }

    @Override
    public void deleteById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        query.executeUpdate();
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b FROM Book b", Book.class);// LEFT JOIN FETCH b.comments", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.id = :id", Book.class);//)// join fetch b.comments where b.id = :id", Book.class);
        query.setParameter("id", id);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        List<Book> listQ = query.getResultList();
        return listQ.size() == 0 ? Optional.empty() : Optional.of(listQ.get(0));
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.genre.type = :genre", Book.class);
        query.setParameter("genre", genre.getType());
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.author.name = :name and b.author.surname = :surname", Book.class);
        query.setParameter("name", author.getName());
        query.setParameter("surname", author.getSurname());
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(Book book) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.title = :title and  b.author.name = :name and b.author.surname = :surname", Book.class);
        query.setParameter("name", book.getAuthor().getName());
        query.setParameter("surname", book.getAuthor().getSurname());
        query.setParameter("title", book.getTitle());
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        List<Book> listQ = query.getResultList();
        return listQ.size() == 0 ? Optional.empty() : Optional.of(listQ.get(0));
    }

}


