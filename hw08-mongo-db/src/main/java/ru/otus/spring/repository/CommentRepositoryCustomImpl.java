package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public void deleteAllByBook(String title, String name, String surname) {
        mongoTemplate.findAllAndRemove(Query.query(Criteria
                .where("book.title").is(title)
                .and("book.author.name").is(name)
                .and("book.author.surname").is(surname)), Comment.class);
    }
}
