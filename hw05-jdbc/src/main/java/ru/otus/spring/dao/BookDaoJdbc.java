package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Book insert(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        book.setId(namedParameterJdbcOperations.query("insert into Books (title, author_id, genre_id) values(:title,:author_id,:genre_id) returning id", params,
                (resultSet, i) -> (resultSet.getLong("id"))).get(0));
        return book;
    }

    @Override
    public int count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(*) from Books", Integer.class);
    }


    @Override
    public void updateByTitle(Book book) {
        namedParameterJdbcOperations.update("update Books set title = :title where id= :id", new BeanPropertySqlParameterSource(book));
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from Books where id = :id", Map.of("id", id));

    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcOperations.query("select bk.id, bk.title,  bk.author_id, bk.genre_id, " +
                "ath.name, ath.surname, g.type " +
                "from Books as bk LEFT JOIN Authors as ath on bk.author_id = ath.id " +
                "LEFT JOIN genres g on bk.genre_id = g.id", bookRowMapper);
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            return Optional.ofNullable(namedParameterJdbcOperations.queryForObject(
                    "select bk.id, bk.title,  bk.author_id, bk.genre_id, " +
                            "ath.name, ath.surname, g.type " +
                            "from Books as bk LEFT JOIN Authors as ath on bk.author_id = ath.id " +
                            "LEFT JOIN genres g on bk.genre_id = g.id where bk.id = :id", Map.of("id", id), bookRowMapper));
        } catch (EmptyResultDataAccessException ex) {
            log.error("{}, {}", ex.getCause(), ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(Book book) {
        List<Book> books = namedParameterJdbcOperations.query(
                "select bk.id, bk.title,  bk.author_id, bk.genre_id, " +
                        "ath.name, ath.surname, g.type " +
                        "from Books as bk LEFT JOIN Authors as ath on bk.author_id = ath.id " +
                        "LEFT JOIN genres g on bk.genre_id = g.id where bk.title = :title and ath.name= :name and ath.surname= :surname",
                Map.of("title", book.getTitle(), "name", book.getAuthor().getName(), "surname", book.getAuthor().getSurname()), bookRowMapper);
        return books.size() == 0 ? Optional.empty() : Optional.of(books.get(0));
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        return namedParameterJdbcOperations.query(
                "select bk.id, bk.title,  bk.author_id, bk.genre_id, " +
                        "ath.name, ath.surname, g.type " +
                        "from Books as bk LEFT JOIN Authors as ath on bk.author_id = ath.id " +
                        "LEFT JOIN genres g on bk.genre_id = g.id where g.type = :type", Map.of("type", genre.getType()), bookRowMapper);

    }

    @Override
    public List<Book> findByAuthor(Author author) {
        return namedParameterJdbcOperations.query(
                "select bk.id, bk.title,  bk.author_id, bk.genre_id, " +
                        "ath.name, ath.surname, g.type " +
                        "from Books as bk LEFT JOIN Authors as ath on bk.author_id = ath.id " +
                        "LEFT JOIN genres g on bk.genre_id = g.id" +
                        " where ath.name = :authorName and ath.surname = :authorSurname", Map.of("authorName", author.getName(), "authorSurname", author.getSurname()), bookRowMapper);
    }

    private final RowMapper<Book> bookRowMapper = (resultSet, i) ->
            new Book(resultSet.getLong("id"), resultSet.getString("title"),
                    new Author(resultSet.getLong("author_id"), resultSet.getString("name"), resultSet.getString("surname")),
                    new Genre(resultSet.getLong("genre_id"), resultSet.getString("type")));

}


