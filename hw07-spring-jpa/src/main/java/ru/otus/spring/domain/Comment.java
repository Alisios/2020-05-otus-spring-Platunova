package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = "id")
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
    public String toString() {
        return "Комментарий: \"" + text + "\" (id " + id + ')' + " к книге: " + book;
    }

    public String toStringWithoutBook() {
        return "Комментарий: \"" + text + "\" (id " + id + ")";
    }
}
