package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    List<Book> listOfBook;

    public Genre(String type) {
        this.type = type;
    }

    public Genre(long id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return type + " (id " + id + ')';
    }

}
