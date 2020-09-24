package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.util.List;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final AuthorRepository authorRepository;

    private final CommentRepository commentRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public void deleteBookByTitleAndAuthor(String title, String name, String surname) {
        Book book = mongoTemplate.findAndRemove(Query.query(Criteria.where("title").is(title)
                .and("author.name").is(name)
                .and("author.surname").is(surname)), Book.class);
        if (book != null) {
            commentRepository.deleteAllByBookId(book.getId());
            List<String> newBooks = authorRepository.findById(book.getAuthor().getId()).orElseThrow().getListOfBookTitles();
            newBooks.remove(title);
            mongoTemplate.update(Author.class)
                    .matching(new Query(Criteria.where("id").is(book.getAuthor().getId())))
                    .apply(new Update().set("listOfBookTitles", newBooks))
                    .findAndModifyValue();
        } else {
            throw new RuntimeException("Книга с такими параметрами не найдена");
        }
    }

    @Override
    @Transactional
    public void deleteBooksByAuthor(String name, String surname) {
        List<Book> books = mongoTemplate.findAllAndRemove(Query.query(Criteria.where("author.name").is(name).and("author.surname").is(surname)), Book.class);
        if (!books.isEmpty()) {
            books.forEach(book -> commentRepository.deleteAllByBookId(book.getId()));
            books.get(0).getAuthor().getListOfBookTitles().clear();
            authorRepository.updateBookTitleList(books.get(0).getAuthor());
        }
    }

}
