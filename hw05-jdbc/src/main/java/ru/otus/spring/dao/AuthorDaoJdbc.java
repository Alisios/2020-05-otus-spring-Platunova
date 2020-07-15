package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", author.getName());
        params.addValue("surname", author.getSurname());
        author.setId(namedParameterJdbcOperations.query("insert into Authors (name, surname) values(:name,:surname) returning id", params,
                (resultSet, i) -> (resultSet.getLong("id"))).get(0));
        return author;
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
        List<Author> authors = (namedParameterJdbcOperations.query("select id, name, surname from Authors where id = :id", Map.of("id", id), authorRowMapper));
        return authors.size() == 0 ? Optional.empty() : Optional.of(authors.get(0));
    }

    @Override
    public Optional<Author> findByFullName(String name, String surname) {
        List<Author> authors = namedParameterJdbcOperations.query("select id, name, surname from Authors where name = :name and surname = :surname", Map.of("name", name, "surname", surname), authorRowMapper);
        return authors.size() == 0 ? Optional.empty() : Optional.of(authors.get(0));
    }
}
