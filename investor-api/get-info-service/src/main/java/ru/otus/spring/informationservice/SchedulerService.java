package ru.otus.spring.informationservice;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.spring.common.ErrorMessage;
import ru.otus.spring.kafka.Producer;

/**
 * Сервис предназначен для периодического опроса биржи и посылки полученной информации в кафку
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final InformationService<StockInfo> informationService;

    private final Producer publisher;

    @Scheduled(fixedRate = 60000)//(cron = "0 * * * * ?")
    void getInfoFromExchange() {
        try {
            publisher.sendInfo(informationService.getStockInfoForKafka());
        } catch (Exception ex) {
            log.error("Ошибка при получении информации с биржи!", ex);
            publisher.sendError(new ErrorMessage(ex, "","Ошибка при получении информации с биржи!"));
        }
    }
}
