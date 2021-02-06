package ru.otus.spring.informationservice;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.spring.kafka.Producer;

/**
 * Сервис предназначен для периодического опроса биржи и посылки полученной информации в кафку
 */

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final InformationService<StockInfo> informationService;

    private final Producer publisher;

    @Scheduled(fixedRate = 60000)//(cron = "0 * * * * ?")
    void getInfoFromExchange() {
        publisher.sendInfo(informationService.getStockInfoForKafka());
    }
}
