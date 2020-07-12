package ru.otus.spring.dao;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют, что репозиторий жанров:")
@DataJpaTest
@Import(CommentDaoHibernate.class)
@Transactional
class CommentDaoHibernateTest {

    @Autowired
    private CommentDaoHibernate commentDaoHibernate;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("корректно создает новый комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyInsertComment() {
        Book book = em.find(Book.class, 1L);
        Comment comment = new Comment(book, "супер!");
        assertThat(commentDaoHibernate.insert(comment))
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("book", book)
                .hasFieldOrPropertyWithValue("text", "супер!");
    }


    @Test
    @DisplayName("корректно вставляет тот же самый комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyInsertTheSameComment() {
        Book book = em.find(Book.class, 1L);
        Comment comment = new Comment(book, "супер!");
        long count1 = commentDaoHibernate.count();
        commentDaoHibernate.insert(comment);
        assertThat(commentDaoHibernate.count()).isEqualTo(count1 + 1);
        em.detach(comment);
        commentDaoHibernate.insert(new Comment(book, "супер!"));
        assertThat(commentDaoHibernate.count()).isEqualTo(count1 + 2);
    }

    @Test
    @DisplayName("корректно обновляет комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyUpdateComment() {
        Book book = em.find(Book.class, 1L);
        Comment comment = new Comment(book, "супер!");
        Comment commentNew = commentDaoHibernate.insert(comment);
        em.detach(commentNew);
        commentNew.setText("Сказка народная");
        commentDaoHibernate.update(comment);
        assertThat(commentDaoHibernate.findById(commentNew.getId()).get())
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("text", commentNew.getText());
    }

    @Test
    @DisplayName("кидает исключение при нулевом комментарии")
    void correctlyThrowExceptions() {
        assertThrows(NullPointerException.class, () -> {
            commentDaoHibernate.insert(null);
        });
        assertThrows(NullPointerException.class, () -> {
            commentDaoHibernate.update(null);
        });
    }


    @Test
    @DisplayName("корректно удаляет комментарий по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyDeleteCommentById() {
        long count1 = commentDaoHibernate.count();
        Comment comment = commentDaoHibernate.findById(1L).get();
        commentDaoHibernate.deleteById(1L);
        assertThat(commentDaoHibernate.count()).isEqualTo(count1 - 1);
        assertThat(commentDaoHibernate.findAll()).doesNotContain(comment);
        assertDoesNotThrow(() -> commentDaoHibernate.deleteById(321L));
    }

    @Test
    @DisplayName("корректно удаляет комментарий по id книги")
    void deleteByBookId() {
        long count1 = commentDaoHibernate.count();
        Comment comment = commentDaoHibernate.findById(7L).get();
        commentDaoHibernate.deleteByBookId(2L);
        assertThat(commentDaoHibernate.count()).isEqualTo(count1 - 1);
        assertThat(commentDaoHibernate.findAll()).doesNotContain(comment);
        assertDoesNotThrow(() -> commentDaoHibernate.deleteByBookId(321L));
    }

    @Test
    @DisplayName("корректно выдает все комментарии")
    void correctlyFindingAllComments() {
        assertThat(commentDaoHibernate.findAll().size()).isNotNull().isEqualTo(7);
    }

    @DisplayName("загружает список всех книг с полной информацией о них (без комментариев)")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyReturnCorrectCommentsListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        System.out.println("\n\n\n\n----------------------------------------------------------------------------------------------------------");
        val comments = commentDaoHibernate.findAll();
        assertThat(comments).isNotNull().hasSize(7)
                .allMatch(s -> !s.getText().equals(""))
                .allMatch(s -> s.getBook() != null);
        System.out.println("----------------------------------------------------------------------------------------------------------\n\n\n\n");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("корректно считает все комментарии")
    void correctlyCountAllGenres() {
        assertThat(commentDaoHibernate.count()).isNotNull().isEqualTo(7);
    }


    @Test
    @DisplayName("корректно находит комментарий по id и не кидает исключение при отсуствии id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyFindById() {
        Comment comment = em.find(Comment.class, 1L);
        assertThat(commentDaoHibernate.findById(1L).get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("book", comment.getBook())
                .hasFieldOrPropertyWithValue("text", comment.getText());
        assertDoesNotThrow(() -> {
            assertThat(commentDaoHibernate.findById(312)).isEmpty();
        });
    }


    @Test
    @DisplayName("находит комментарий по содержанию и не кидает исключение при отсуствии")
    void findCommentByText() {
        Comment comm = em.find(Comment.class, 1L);
        assertThat(commentDaoHibernate.findByText("Классная"))
                .isNotEmpty()
                .contains(comm);
        assertThat(commentDaoHibernate.findByText("dsdfa")).isEmpty();
    }

    @Test
    @DisplayName("находит комментарий по книге и не кидает исключение при отсуствии")
    void findCommentByBook() {
        Book book = em.find(Book.class, 1L);
        Comment comm = em.find(Comment.class, 1L);
        assertThat(commentDaoHibernate.findByBook(book))
                .isNotNull()
                .contains(comm)
                .size().isEqualTo(4);
        assertThat(commentDaoHibernate.findByBook(new Book("Страж", new Author("Алексей", "Пехов"), new Genre("фэнтези")))).isEmpty();
    }

    @Test
    @DisplayName("находит комментарий по id книги и не кидает исключение при отсуствии")
    void findCommentByBookId() {
        Comment comm = em.find(Comment.class, 1L);
        assertThat(commentDaoHibernate.findByBookId(1L))
                .isNotNull()
                .contains(comm)
                .size().isEqualTo(4);
        assertDoesNotThrow(() -> {
            assertThat(commentDaoHibernate.findByBookId(312)).isEmpty();
        });
    }
}