package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "comments")
@NamedEntityGraph(name = "book-comment-entity-graph",
        attributeNodes = {@NamedAttributeNode("book")})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    long id;

    @Column(name = "text")
    String text;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    Book book;

    public Comment(Book book, String text) {
        this.book = book;
        this.text = text;
    }

    public Comment(long id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(text, comment.text) &&
                Objects.equals(book, comment.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, book);
    }

    @Override
    public String toString() {
        return "Комментарий: \"" + text + "\" (id " + id + ')' + " к книге: " + book;
    }

    public String toStringWithoutBook() {
        return "Комментарий: \"" + text + "\" (id " + id + ")";
    }
}
