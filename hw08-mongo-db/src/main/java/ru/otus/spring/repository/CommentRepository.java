package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Comment;

import java.util.Date;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {

    Comment save(Comment comment);

    List<Comment> findAll();

    void deleteAllByBookId(String id);

    List<Comment> findAllByTextContainingIgnoreCase(String text);

    List<Comment> findAllByDateAfter(Date date);

    List<Comment> findAllByDateBefore(Date date);

    List<Comment> findAllByBook_TitleIgnoreCaseAndBook_Author_NameIgnoreCaseAndBook_Author_SurnameIgnoreCase(String title, String name, String surname);//AllIgnoreCase(String title, String name, String surname);

    Comment findFirstByBook_TitleIgnoreCaseAndBook_Author_NameIgnoreCaseAndBook_Author_SurnameIgnoreCase_AndTextContainingIgnoreCase(String title, String name, String surname, String text);//AllIgnoreCase(String title, String name, String surname);

    void deleteByBook_TitleIgnoreCaseAndBook_Author_NameIgnoreCaseAndBook_Author_SurnameIgnoreCaseAndAndTextContainingIgnoreCase(String title, String name, String surname, String text);

}
