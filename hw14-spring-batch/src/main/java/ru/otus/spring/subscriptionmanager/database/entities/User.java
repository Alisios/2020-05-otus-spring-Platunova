package ru.otus.spring.subscriptionmanager.database.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_sub")
@EqualsAndHashCode(exclude = "subscriptions")
public class User {

    @Id
    @GeneratedValue//(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @org.hibernate.annotations.Type(type = "pg-uuid")
    UUID id;

    @Column(name = "user_real_id")
    @NotNull
    String user_real_id;

    @Column(name = "email")
    String email;

    @Column(name = "telegram")
    String telegram;

    @Column(name = "max")
    double max;

    @Column(name = "min")
    double min;

    @Column(name = "change")
    double change;

    @ManyToMany(mappedBy = "users")
    @JsonBackReference
    @ToString.Exclude
    Set<Subscription> subscriptions = new HashSet<>();

    @JsonBackReference
    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

}
