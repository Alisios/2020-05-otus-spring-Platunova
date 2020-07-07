package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final RowMapper<Author> authorRowMapper = (resultSet, i) ->
            new Author(resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"));

    @Override
    public Author insert(Author author) {
        namedParameterJdbcOperations.update("insert into Authors (name, surname) values (:name, :surname)", new BeanPropertySqlParameterSource(author));
        return findByFullName(author.getName(), author.getSurname()).get();
    }

    @Override
    public int count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(*) from Authors", Integer.class);
    }


    @Override
    public void update(Author author) {
        namedParameterJdbcOperations.update("update Authors set name = :name, surname = :surname where id= :id", new BeanPropertySqlParameterSource(author));
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from Authors where id = :id", Map.of("id", id));

    }

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcOperations.query("select id, name, surname from Authors", authorRowMapper);
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject("select id, name, surname from Authors where id = :id", Map.of("id", id), authorRowMapper));
        } catch (EmptyResultDataAccessException ex) {
            log.error("{}, {}", ex.getCause(), ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Author> findByFullName(String name, String surname) {
        if (namedParameterJdbcOperations.query("select id, name, surname from Authors where name = :name and surname = :surname", Map.of("name", name, "surname", surname), authorRowMapper).size() == 0)
            return Optional.empty();
        return Optional.of(namedParameterJdbcOperations.query("select id, name, surname from Authors where name = :name and surname = :surname", Map.of("name", name, "surname", surname), authorRowMapper).get(0));
    }
}
