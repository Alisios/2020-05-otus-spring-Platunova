package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.User;
import ru.otus.spring.repository.ServiceException;
import ru.otus.spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new ServiceException("Error with saving user " + user.toString(), ex);
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ServiceException("Error with deleting user with id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        try {
            return userRepository.findAll();
        } catch (Exception ex) {
            throw new ServiceException("Error with finding all users", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getById(long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception ex) {
            throw new ServiceException("Error with finding user by id " + id, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getByName(String name, String surname) {
        try {
            return userRepository.findByNameAndSurname(name, surname);
        } catch (Exception ex) {
            throw new ServiceException("Error with finding user by name " + name + " " + surname, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getByLogin(String login) {
        try {
            return userRepository.findByLogin(login);
        } catch (Exception ex) {
            throw new ServiceException("Error with finding user by login-password " + login, ex);
        }
    }
}
