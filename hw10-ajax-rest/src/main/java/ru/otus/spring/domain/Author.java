package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authors")
@EqualsAndHashCode(of = "id")
public class Author implements Serializable {
    private static final long serialVersionUID = 129348786L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    List<Book> listOfBook = new ArrayList<>();

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Author(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }


    @Override
    public String toString() {
        return name +
                " " + surname + " (" +
                "id " + id +
                ")";
    }

}
