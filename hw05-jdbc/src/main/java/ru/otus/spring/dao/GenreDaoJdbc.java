package ru.otus.spring.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final RowMapper<Genre> genreRowMapper = (resultSet, i) ->
            new Genre(resultSet.getLong("id"),
                    resultSet.getString("type"));

    @Override
    public Genre insert(Genre genre) {
        namedParameterJdbcOperations.update("insert into Genres (type) values (:type)", new BeanPropertySqlParameterSource(genre));
        return findByType(genre.getType()).get();
    }

    @Override
    public int count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(*) from Genres", Integer.class);
    }

    @Override
    public void update(Genre genre) {
        namedParameterJdbcOperations.update("update Genres set type = :type where id= :id", new BeanPropertySqlParameterSource(genre));
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from Genres where id = :id", Map.of("id", id));
    }

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbcOperations.query("select id, type from Genres", genreRowMapper);
    }

    @Override
    public Optional<Genre> findById(long id) {
        try {
            return Optional.of(namedParameterJdbcOperations.queryForObject("select id, type from Genres where id = :id", Map.of("id", id), genreRowMapper));
        } catch (EmptyResultDataAccessException ex) {
            log.error("{}, {}", ex.getCause(), ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Genre> findByType(String type) {
        if (namedParameterJdbcOperations.query("select id, type from Genres where type = :type", Map.of("type", type), genreRowMapper).size() == 0)
            return Optional.empty();
        else
            return Optional.of(namedParameterJdbcOperations.query("select id, type from Genres where type = :type", Map.of("type", type), genreRowMapper).get(0));
    }
}
