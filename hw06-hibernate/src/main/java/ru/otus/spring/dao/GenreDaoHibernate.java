package ru.otus.spring.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class GenreDaoHibernate implements GenreDao {

    @PersistenceContext
    final private EntityManager em;

    @Override
    public Genre insert(Genre genre) {
        if (genre.getId() <= 0) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    @Override
    public void update(Genre genre) {
        Query query = em.createQuery("update Genre g set g.type = :type where g.id = :id");
        query.setParameter("type", genre.getType());
        query.setParameter("id", genre.getId());
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        Query query = em.createQuery("delete from Genre g where g.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public long count() {
        return em.createQuery("select count(b) from Genre b", Long.class).getSingleResult();
    }

    @Override
    public List<Genre> findAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        try {
            return Optional.of(em.find(Genre.class, id));
        } catch (NullPointerException ex) {
            log.error("{}, {}", ex.getCause(), ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Genre> findByType(String type) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.type = :type", Genre.class);
        query.setParameter("type", type);
        List<Genre> listQ = query.getResultList();
        return listQ.size() == 0 ? Optional.empty() : Optional.of(listQ.get(0));
    }
}
