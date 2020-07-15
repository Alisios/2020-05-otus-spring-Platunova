package ru.otus.spring.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        genre.setId(namedParameterJdbcOperations.query("insert into Genres (type) values (:type) returning id", Map.of("type", genre.getType()),
                (resultSet, i) -> (resultSet.getLong("id"))).get(0));
        return genre;
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
        List<Genre> genres = namedParameterJdbcOperations.query("select id, type from Genres where id = :id", Map.of("id", id), genreRowMapper);
        return genres.size() == 0 ? Optional.empty() : Optional.of(genres.get(0));
    }

    @Override
    public Optional<Genre> findByType(String type) {
        List<Genre> genres = namedParameterJdbcOperations.query("select id, type from Genres where type = :type", Map.of("type", type), genreRowMapper);
        return genres.size() == 0 ? Optional.empty() : Optional.of(genres.get(0));
    }
}
