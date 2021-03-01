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
import ru.otus.spring.stockinfoservice.database.dto.StockDtoCurrent;
import ru.otus.spring.stockinfoservice.database.dto.StockMapper;
import ru.otus.spring.stockinfoservice.database.entity.StockEntityCurrent;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StockCurrentServiceDbImpl implements StockCurrentServiceDb {

    StockCurrentRepository stockRepository;
    StockMapper mapper;
    Comparator<StockEntityCurrent> comparator = Comparator.comparing(StockEntityCurrent::getDate);


    @Override
    @Transactional
    public void addStock(StockDtoCurrent stockDtoCurrent) {
        try {
            stockRepository.save(mapper.map(stockDtoCurrent));
            log.info("Информация по компании {} успешно сохранена", stockDtoCurrent);
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при сохранении акции в архив: " + stockDtoCurrent, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StockEntityCurrent getById(String id) {
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
    public void deleteStock(StockDtoCurrent stockDtoCurrent) {
        if (stockDtoCurrent == null)
            throw new ValidationException("Введите корректные данные запроса");
        try {
            stockRepository.deleteById(stockDtoCurrent.getId());
            log.info("Компания {} удалена", stockDtoCurrent);
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при удалении компании из архива: " + stockDtoCurrent, ex);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<StockEntityCurrent> getAllStocks() {
        try {
            return stockRepository.findAll();
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о всех компаниях: ", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockEntityCurrent> getByInterval(String ticker, LocalDateTime before, LocalDateTime after) {
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
    @Transactional(readOnly = true)
    public StockEntityCurrent getLastInfoByTicker(String ticker) {
        if (ticker == null)
            throw new ValidationException("Введите корректные данные запроса");
        try {
            return stockRepository.findTopByTickerOrderByDateDesc(ticker).orElseThrow(() -> new StockDbServiceException("Ошибка при получении информации о компании : " + ticker));
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о компании : " + ticker, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StockEntityCurrent getLastInfoByCompanyName(String companyName) {
        if (companyName == null)
            throw new ValidationException("Введите корректные данные запроса");
        try {
            return stockRepository.findTopByCompanyName(companyName).orElseThrow(() -> new StockDbServiceException("Ошибка при получении информации о компании : " + companyName));
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о компании : " + companyName, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockEntityCurrent> getByType(String type) {
        if (type == null)
            throw new ValidationException("Введите корректные данные запроса");
        try {
            Map<String, Optional<StockEntityCurrent>> map3 = stockRepository.findAllByType(type).stream()
                    .collect(Collectors.groupingBy(StockEntityCurrent::getCompanyName, Collectors.maxBy(comparator)));
            return map3.values().stream().map(Optional::get).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о компанииях с типом : " + type, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockDto> getAllLastStocks() {
        try {
            List<StockEntityCurrent> list = stockRepository.findAllByOrderByDateDesc();
            Set<String> tickerSet = list.stream().map(StockEntityCurrent::getTicker).collect(Collectors.toSet());
            List<StockEntityCurrent> res = new ArrayList<>();
            tickerSet.forEach(ticker -> res.add(list.stream().filter(e -> e.getTicker().equalsIgnoreCase(ticker)).findFirst().get()));
            return res.stream().map(mapper::fromCurrentEntityToStockDto).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при получении информации о компанииях за последний день", ex);
        }
    }


    @Override
    @Transactional
    public void clear() {
        try {
            stockRepository.deleteAll();
        } catch (Exception ex) {
            throw new StockDbServiceException("Ошибка при удалении всех текущих акций", ex);
        }
    }
}

