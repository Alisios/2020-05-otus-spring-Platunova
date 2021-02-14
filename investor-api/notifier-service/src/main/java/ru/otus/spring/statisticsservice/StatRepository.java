package ru.otus.spring.statisticsservice;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatRepository extends CrudRepository<StatElement, String> {

    List<StatElement> findAll();

}
