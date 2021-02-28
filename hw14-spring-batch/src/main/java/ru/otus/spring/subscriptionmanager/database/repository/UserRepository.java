package ru.otus.spring.subscriptionmanager.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.subscriptionmanager.database.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("FROM User u WHERE u.user_real_id=:id")
    Optional<User> findByUser_id(String id);
}
