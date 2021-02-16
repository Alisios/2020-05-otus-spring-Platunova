package ru.otus.spring.typeservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.classifyservice.StockInfo;
import ru.otus.spring.classifyservice.checkservice.CheckService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Сервис, определяющий тип ценной бумаги исходя из названия //заменитЬ!! упрощенно)
 */
@Service
@Slf4j
class TypeServiceByName implements TypeService {

    private final Map<String, CheckService> handlerMap = new HashMap<>();

    TypeServiceByName(List<CheckService> checkService) {
        checkService.forEach(h -> handlerMap.put(h.getType(), h));
    }

    @Override
    public String classify(StockInfo stockInfo) {
        CheckService type = handlerMap.values().stream().filter(k -> k.checkType(stockInfo)).findFirst().orElseThrow(() -> new TypeServiceException("Ошибка определения типа ценнной бумаги!"));
        return type.getType();
    }

}

