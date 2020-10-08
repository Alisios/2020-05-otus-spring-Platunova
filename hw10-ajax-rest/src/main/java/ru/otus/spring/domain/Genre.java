package ru.otus.spring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "genres")
public class Genre implements Serializable {
    private static final long serialVersionUID = 129348789L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonBackReference
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
