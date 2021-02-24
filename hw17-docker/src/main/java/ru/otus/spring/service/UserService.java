package ru.otus.spring.service;

import ru.otus.spring.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User author);

    void deleteById(long id);

    List<User> getAll();

    Optional<User> getById(long id);

    List<User> getByName(String name, String surname);

    Optional<User> getByLogin(String login);
}
