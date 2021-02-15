package ru.otus.spring.classifyservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import ru.otus.spring.sectorservice.SectorDbService;
import ru.otus.spring.typeservice.TypeService;
import ru.otus.spring.typeservice.TypeServiceException;


/**
 * сервис для классификации ценных бумаг: по типу и по сектору экономики,
 * которому принадлежит компания
 */
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClassifierServiceType implements ClassifierService {

    SectorDbService sectorDbService;
    TypeService typeService;

    @Override
    public String defineType(StockInfo stockInfo) {
        try {
            return typeService.classify(stockInfo);
        } catch (Exception ex) {
            log.error("Ошибка определения типа ценной бумаги! Сообщение не может быть обработано", ex);
            throw new TypeServiceException("Ошибка определения типа ценной бумаги! Сообщение не может быть обработано", ex);
        }
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public String defineSector(StockInfo stockInfo) {
        return sectorDbService.getSectorByTicker(stockInfo.getTicker());
    }
}
