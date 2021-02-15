package ru.otus.spring.informationservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.spring.dispatcher.InfoDispatcher;


/**
 * сервис предназанчен для полулчения информации с биржи в формате StockInfo
 */
@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InformationServiceStockInfo implements InformationService<StockInfo> {

    InfoDispatcher infoDispatcher;
    InfoDispatcher infoDispatcherCache;

    @Autowired
    InformationServiceStockInfo(@Qualifier("infoDispatcherRest") InfoDispatcher infoDispatcher,
                                @Qualifier("infoDispatcherDefault") InfoDispatcher infoDispatcherCache) {
        this.infoDispatcher = infoDispatcher;
        this.infoDispatcherCache = infoDispatcherCache;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getStockInfoForKafkaOptional", commandKey = "getInfo", groupKey = "getInfo")
    public StockInfo getStockInfoForKafka() {
        return infoDispatcher.getInfo();

    }

    private StockInfo getStockInfoForKafkaOptional() {
        return infoDispatcherCache.getInfo();
    }

}
