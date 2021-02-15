package ru.otus.spring.typeservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.classifyservice.StockInfo;
import ru.otus.spring.configuration.TypeProperties;


/**
 * Сервис, определяющий тип ценной бумаги исходя из названия //заменитЬ!! упрощенно)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class TypeServiceByName implements TypeService {

    TypeProperties typeProperties;

    @Override
    public String classify(StockInfo stockInfo) {
        String name = stockInfo.getCompanyName();
        if (checkIfExchange(stockInfo.getExchange()))
            return typeProperties.getETF();
        if (checkIfCurrency(name))
            return typeProperties.getCurrency();
        if (checkIfBond(name))
            return typeProperties.getBond();
        if (checkIfStock(name))
            return typeProperties.getStock();
        throw new TypeServiceException("Ошибка определения типа ценнной бумаги!");
    }

    private boolean checkIfExchange(String exchange) {
        return (exchange != null);
    }

    private boolean checkIfCurrency(String name) {
        return name.contains("_Cur");
    }

    private boolean checkIfBond(String name) {
        return name.contains("_Bond");
    }

    private boolean checkIfStock(String name) {
        return name.contains("_Stock");
    }
}

