package ru.otus.spring.subscriptionmanager.database.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "subscriptions")
@EqualsAndHashCode(exclude = "users")
@NamedEntityGraph(name = "sub-user-graph",
        attributeNodes = {@NamedAttributeNode("users")})
public class Subscription {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "ticker")
    String ticker;

    @Column(name = "typeEvent")
    String typeEvent;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "sub_user",
            joinColumns = @JoinColumn(name = "sub_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    Set<User> users = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
        user.getSubscriptions().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getSubscriptions().remove(this);
    }


    @JsonManagedReference
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
