package ru.otus.spring.subscriptionmanager.database.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.subscriptionmanager.database.entities.Subscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    @EntityGraph(value = "sub-user-graph")
    @Query("FROM Subscription u WHERE u.ticker=:ticker")
    List<Subscription> findByTicker(String ticker);

    @EntityGraph(value = "sub-user-graph")
    @Query("FROM Subscription u WHERE u.id=:id")
    Optional<Subscription> findById(UUID id);

    @EntityGraph(value = "sub-user-graph")
    List<Subscription> findAll();

    Subscription save(Subscription subscription);

    Subscription deleteByTicker(String ticker);

    @EntityGraph(value = "sub-user-graph")
    @Query("FROM Subscription u WHERE u.ticker=:ticker and u.typeEvent=:typeEvent")
    Optional<Subscription> findByTickerAndTypeEvent(String ticker, String typeEvent);


}
