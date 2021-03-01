package ru.otus.spring.stockinfoservice.database;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.common.StockDbServiceException;
import ru.otus.spring.common.ValidationException;
import ru.otus.spring.stockinfoservice.database.dto.StockDto;
import ru.otus.spring.stockinfoservice.database.dto.StockMapper;
import ru.otus.spring.stockinfoservice.database.entity.StockEntity;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StockServiceDbImpl implements StockServiceDb {

    StockRepository stockRepository;
    StockMapper mapper;
    Comparator<StockEntity> comparator = Comparator.comparing(StockEntity::getDate);


    @Override
    @Transactional
    public void addStock(StockDto stockDto) {
        try {
            stockRepository.save(mapper.map(stockDto));
            log.info("Информация по компании {} успешно сохранена", stockDto);
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при сохранении акции в архив: " + stockDto, ex);
        }
    }

    @Override
    @Transactional
    public void addAllStocks(List<StockDto> stockDtos) {
        try {
            List<StockEntity> newStocks = stockDtos.stream().map(mapper::map).collect(Collectors.toList());
            stockRepository.saveAll(newStocks);
            log.info("Последняя информация пл компании успешно созхранена ");
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при сохранении акций в архив", ex);
        }
    }

    @Override
    @Transactional
    public StockEntity getById(String id) {
        if (id == null)
            throw new ValidationException("Введите корректные данные запроса");
        try {
            return stockRepository.findById(id)
                    .orElseThrow(() -> new StockDbServiceException("Информация по компании не найдена"));
        } catch (Exception ex) {
            throw new StockDbServiceException("Информация по акции c id " + id + " не найдена: ", ex);
        }
    }


    @Override
    @Transactional
    public void deleteStock(StockDto stockDto) {
        if (stockDto == null)
            throw new ValidationException("Введите корректные данные запроса");
        try {
            stockRepository.deleteById(stockDto.getId());
            log.info("Компания {} удалена", stockDto);
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при удалении компании из архива: " + stockDto, ex);
        }
    }


    @Override
    @Transactional
    public List<StockEntity> getAllStocks() {
        try {
            return stockRepository.findAll();
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о всех компаниях: ", ex);
        }
    }

    @Override
    @Transactional
    public List<StockEntity> getByInterval(String ticker, LocalDateTime before, LocalDateTime after) {
        //Sort sort = Sort.by(Sort.Direction.ASC, "date");
        if ((ticker == null) || (before == null) || (after == null))
            throw new ValidationException("Введите корректные данные запроса");
        if (before.isAfter(after))
            throw new ValidationException("Первая дата не может быть больше второй");
        if (after.isAfter(LocalDateTime.now()) || before.isAfter(LocalDateTime.now()))
            throw new ValidationException("Даты должны быть меньше текущей даты");
        try {
            return stockRepository.findAllByTickerAndDateBetweenOrderByDateDesc(ticker, before, after);
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о компании " + ticker + " в заданном интервале: " + before + " - " + after, ex);
        }
    }

    @Override
    @Transactional
    public StockEntity getLastInfoByTicker(String ticker) {
        if (ticker == null)
            throw new ValidationException("Введите корректные данные запроса");
        try {
            return stockRepository.findAllByTickerOrderByDateDesc(ticker).get(0);
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о компании : " + ticker, ex);
        }
    }

    @Override
    @Transactional
    public List<StockEntity> getLastInfoByCompanyName(String companyName) {
        if (companyName == null)
            throw new ValidationException("Введите корректные данные запроса");
        try {
            return stockRepository.findAllByCompanyName(companyName);
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о компании : " + companyName, ex);
        }
    }

    @Override
    @Transactional
    public List<StockEntity> getByType(String type) {
        if (type == null)
            throw new ValidationException("Введите корректные данные запроса");
        try {
            Map<String, Optional<StockEntity>> map3 = stockRepository.findAllByType(type).stream()
                    .collect(Collectors.groupingBy(StockEntity::getCompanyName, Collectors.maxBy(comparator)));
            return map3.values().stream().map(Optional::get).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о компанииях с типом : " + type, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockEntity> getAllStocksOfLastDay(LocalDateTime localDateTime) {
        try {
            return stockRepository.findAllByDateAfterOrderByDate(localDateTime);
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о компанииях за последний день", ex);
        }
    }
}



