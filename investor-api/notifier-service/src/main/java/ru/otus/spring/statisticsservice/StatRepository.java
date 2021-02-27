package ru.otus.spring.statisticsservice;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.util.List;
import java.util.Optional;

public interface StatRepository extends KeyValueRepository<StatElement, String> {

    List<StatElement> findAll();

    Optional<StatElement> findById(String key);

}
