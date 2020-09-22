package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;

@RequiredArgsConstructor
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

    private final MongoOperations mongoOperations;

    @Override
    @Transactional
    public Author updateBookTitleList(Author author) {
        return mongoOperations.update(Author.class)
                .matching(new Query(Criteria.where("id").is(author.getId())))
                .apply(new Update().set("listOfBookTitles", author.getListOfBookTitles()))
                .withOptions(FindAndModifyOptions.options().upsert(true).returnNew(true))
                .findAndModifyValue();
    }
}
