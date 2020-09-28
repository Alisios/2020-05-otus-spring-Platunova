package ru.otus.spring.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.Date;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDbByData {

    private Author author1;
    private Author author2;
    private Author author3;
    private Author author4;
    private Author author5;
    private Author author6;


    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;
    private Book book5;
    private Book book7;
    private Book book9;
    private Book book10;

    @ChangeSet(order = "001", id = "dropDB", author = "alice", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "alice", runAlways = true)
    public void initAuthors(AuthorRepository template) {
        author1 = template.save(new Author("Джоан", "Роулинг"));
        author2 = template.save(new Author("Артур", "Конан Дойль"));
        author3 = template.save(new Author("Стивен", "Кинг"));
        author4 = template.save(new Author("Рэй", "Брэдберри"));
        author5 = template.save(new Author("Алексей", "Пехов"));
        author6 = template.save(new Author("Харуки", "Мураками"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "alice", runAlways = true)
    public void initBooks(BookRepository template, AuthorRepository template2) {
        book1 = template.save(new Book("Гарри Поттер и тайная комната", author1, new Genre("фэнтези")));
        book5 = template.save(new Book("Гарри Поттер и дары смерти", author1, new Genre("фэнтези")));
        book3 = template.save(new Book("Шерлок Холмс", author2, new Genre("детектив")));
        book4 = template.save(new Book(" Темная Башня", author3, new Genre("ужасы")));
        book2 = template.save(new Book("Гарри Поттер и орден Феникса", author1, new Genre("фэнтези")));
        book7 = template.save(new Book("Страна чудес без тормозов", author6, new Genre("роман")));
        book9 = template.save(new Book("Марсианские хроники", author4, new Genre("фантастика")));
        book10 = template.save(new Book("Страж", author5, new Genre("фэнтези")));

        author1.getListOfBookTitles().addAll(List.of(book1.getTitle(),book5.getTitle(),book2.getTitle()));
        template2.updateBookTitleList(author1);

        author2.getListOfBookTitles().add(book3.getTitle());
        template2.updateBookTitleList(author2);

        author3.getListOfBookTitles().add(book4.getTitle());
        template2.updateBookTitleList(author3);

        author4.getListOfBookTitles().add(book9.getTitle());
        template2.updateBookTitleList(author4);

        author5.getListOfBookTitles().add(book10.getTitle());
        template2.updateBookTitleList(author5);

        author6.getListOfBookTitles().add(book7.getTitle());
        template2.updateBookTitleList(author6);

    }

    @ChangeSet(order = "003", id = "initComments", author = "alice", runAlways = true)
    public void initComments(CommentRepository template) {
        template.save(new Comment(book1, "Классная книга!", new Date()));
        template.save(new Comment(book1, "И мне понравилась", new Date()));
        template.save(new Comment(book1, "Моя любимая", new Date()));
        template.save(new Comment(book1, "А мне больше орден феникса нравится", new Date()));
        template.save(new Comment(book4, "Очень долгая!", new Date()));
        template.save(new Comment(book3, "классика!", new Date()));
        template.save(new Comment(book2, "Сириус лучший!", new Date()));
        template.save(new Comment(book7, "Странная книга как и все у него", new Date()));
        template.save(new Comment(book10, "среди русских фантастов очень хорош", new Date()));
    }
}
