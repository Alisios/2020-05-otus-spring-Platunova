package ru.otus.spring.informationservice;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class SchedulerService {

    InformationService<StockInfo> informationService;

    Producer publisher;

    @Scheduled(cron = "${ru.otus.spring.cron}")
    void getInfoFromExchange() {
        try {
            publisher.sendInfo(informationService.getStockInfoForKafka());
        } catch (Exception ex) {
            log.error("Ошибка при получении информации с биржи!", ex);
            publisher.sendError(new ErrorMessage(ex, "","Ошибка при получении информации с биржи!"));
        }
    }
}
