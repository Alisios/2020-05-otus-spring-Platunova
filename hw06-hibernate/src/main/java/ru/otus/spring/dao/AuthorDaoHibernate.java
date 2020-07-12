package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor

public class AuthorDaoHibernate implements AuthorDao {

    @PersistenceContext
    final private EntityManager em;

    @Override
    public Author insert(Author author) {
        if (author.getId() <= 0) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public void update(Author author) {
        Query query = em.createQuery("update Author a set a.name = :name , a.surname = :surname where a.id = :id");
        query.setParameter("name", author.getName());
        query.setParameter("surname", author.getSurname());
        query.setParameter("id", author.getId());
        query.executeUpdate();
    }

    @Override
    public long count() {
        return em.createQuery("select count(b) from Author b", Long.class).getSingleResult();
    }


    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Author a where a.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            return Optional.of(em.find(Author.class, id));
        } catch (NullPointerException ex) {
            log.error("{}, {}", ex.getCause(), ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Author> findByFullName(String name, String surname) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name = :name and a.surname = :surname", Author.class);
        query.setParameter("name", name);
        query.setParameter("surname", surname);
        List<Author> listQ = query.getResultList();
        return listQ.size() == 0 ? Optional.empty() : Optional.of(listQ.get(0));
    }
}
