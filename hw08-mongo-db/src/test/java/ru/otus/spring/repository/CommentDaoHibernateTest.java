package ru.otus.spring.repository;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase
@DisplayName("Тесты проверяют, что репозиторий жанров:")
@DataJpaTest
@Transactional
class CommentDaoHibernateTest {

    @Autowired
    private CommentRepository commentDaoHibernate;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("кидает исключение при нулевом комментарии")
    void correctlyThrowExceptions() {
        assertThrows(DataAccessException.class, () -> commentDaoHibernate.save(null));
        assertThrows(DataAccessException.class, () -> commentDaoHibernate.save(null));
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
        assertThrows(DataAccessException.class, () -> { commentDaoHibernate.deleteById(321L);});
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


    @DisplayName("загружает список всех книг с полной информацией о них (без комментариев)")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void correctlyReturnCorrectCommentsListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        val comments = commentDaoHibernate.findAll();
        assertThat(comments).isNotNull().hasSize(7)
                .allMatch(s -> !s.getText().equals(""))
                .allMatch(s -> s.getBook() != null);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @DisplayName("находит комментарий по тексту за 1 селект")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findByTextWithBookByOneSelect() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        commentDaoHibernate.findByTextContainingIgnoreCase("классная");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
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
        assertDoesNotThrow(() -> assertThat(commentDaoHibernate.findById(312)).isEmpty());
    }


    @Test
    @DisplayName("находит комментарий по содержанию и не кидает исключение при отсуствии")
    void findCommentByText() {
        Comment comm = em.find(Comment.class, 1L);
        assertThat(commentDaoHibernate.findByTextContainingIgnoreCase("Классная"))
                .isNotEmpty()
                .contains(comm);
        assertThat(commentDaoHibernate.findByTextContainingIgnoreCase("клаССНАя"))
                .isNotEmpty()
                .contains(comm);
        assertThat(commentDaoHibernate.findByTextContainingIgnoreCase("dsdfa")).isEmpty();
    }

    @Test
    @DisplayName("находит комментарий по книге и не кидает исключение при отсуствии")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findCommentByBook() {
        Book book = em.find(Book.class, 1L);
        Comment comm = em.find(Comment.class, 1L);
        assertThat(commentDaoHibernate.findByBook(book.getTitle(), book.getAuthor().getName(), book.getAuthor().getSurname()))
                .isNotNull()
                .contains(comm)
                .size().isEqualTo(4);
        assertThat(commentDaoHibernate.findByBook("Страж","Алексей", "Пехов")).isEmpty();
    }

    @DisplayName("находит комментарий по книге за 1 селект")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findCommentByBookByOneSelect() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        commentDaoHibernate.findByBook("Гарри поттер", "Джоан", "Роулинг");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("находит комментарий по id книги и не кидает исключение при отсуствии")
    void findCommentByBookId() {
        Comment comm = em.find(Comment.class, 1L);
        assertThat(commentDaoHibernate.findByBookId(1L))
                .isNotNull()
                .contains(comm)
                .size().isEqualTo(4);
        assertDoesNotThrow(() -> assertThat(commentDaoHibernate.findByBookId(312)).isEmpty());
    }

    @DisplayName("находит комментарий по id книги вместе с книгой за 1 селект")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findCommentByBookIdByOneSelect() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        commentDaoHibernate.findByBookId(1L);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }
}