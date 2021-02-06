package ru.otus.spring.classifierservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.otus.spring.configuration.TypeProperties;
import ru.otus.spring.database.TypeDbService;

/**
 * сервис для классификации ценных бумаг: по типу и по сектору экономики,
 * которому принадлежит компания
 */
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClassifierServiceType implements ClassifierService {

    TypeDbService typeDbService;
    TypeProperties typeProperties;

    @Override
    public String defineType(StockInfo stockInfo) {
        String name = stockInfo.getCompanyName();
        if (stockInfo.getExchange() == null)
            if (name.contains("Bond"))                   //заменитЬ!! упрощенно)
                return typeProperties.getBond();
            else if (name.contains("Cur"))
                return typeProperties.getCurrency();
            else return typeProperties.getStock();
        else
            return typeProperties.getETF();

    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public String defineSector(StockInfo stockInfo) {
        return typeDbService.getSectorByTicker(stockInfo.getTicker());
    }
}
