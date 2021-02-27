package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {

    User save(User comment);

    void deleteById(long id);

    List<User> findAll();

    Optional<User> findById(long id);

    @Query("FROM User u WHERE u.name=:name and u.surname=:surname")
    List<User> findByNameAndSurname(String name, String surname);

    Optional<User> findByLogin(String login);
}
