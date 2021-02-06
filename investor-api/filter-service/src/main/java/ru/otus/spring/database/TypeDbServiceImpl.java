package ru.otus.spring.database;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.database.entity.StockType;

import java.util.List;
import java.util.UUID;

/**
 * сервис для взаимодействия с репозиторием секторов компании
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class TypeDbServiceImpl implements TypeDbService {

    private final TypeRepository repository;

    @Override
    @Transactional(readOnly = true)
    public String getSectorByTicker(@NonNull String ticker) {
        try {
            return repository.findByTicker(ticker).orElseThrow(() ->
                    new TypeDbServiceException("Ошибка при определении сектора. Заданного тикера не существует!")).getSector();
        } catch (Exception ex) {
            log.error("Ошибка при определении сектора по тикеру компании {}", ticker);
            throw new TypeDbServiceException("Ошибка при определении сектора по тикеру компании", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockType> getAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            log.error("Ошибка при получении всех секторов");
            throw new TypeDbServiceException("Ошибка при получении всех секторов", ex);
        }
    }

    @Override
    @Transactional
    public void delete(@NonNull String id) {
        try {
            repository.deleteById(id);
        } catch (Exception ex) {
            log.error("Ошибка при удалении сектора {}", id);
            throw new TypeDbServiceException("Ошибка при удалении сектора " + id + " .", ex);
        }
    }

    @Override
    @Transactional
    public void add(@NonNull StockType stockType) {
        try {
            stockType.setId(UUID.randomUUID().toString());
            repository.save(stockType);
        } catch (Exception ex) {
            log.error("Ошибка при добавлении сектора {}", stockType);
            throw new TypeDbServiceException("Ошибка при добавлении сектора " + stockType + " .", ex);
        }
    }

}
