package ru.otus.spring.classifyservice.checkservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.classifyservice.StockInfo;
import ru.otus.spring.configuration.TypeProperties;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CheckServiceCurrency implements CheckService {

    TypeProperties typeProperties;

    @Override
    public boolean checkType(StockInfo stockInfo) {
        return stockInfo.getCompanyName().contains("_Cur");
    }

    @Override
    public String getType() {
        return typeProperties.getCurrency();
    }
}
