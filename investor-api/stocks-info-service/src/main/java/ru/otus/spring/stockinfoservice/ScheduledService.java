package ru.otus.spring.stockinfoservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.spring.kafka.Producer;
import ru.otus.spring.stockinfoservice.database.dto.StockDto;
import ru.otus.spring.stockinfoservice.database.dto.StockMapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * сервис который:
 * 1) отправляет обновления в обработчики событий каждый день в
 * определнное время с обновленной информацией
 * 2) обновляет сервис хранения исторчиеских данных каждый день
 * 3) очищает таблицу с текущими данными (сегодняшними)
 */
@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class ScheduledService {

    StockManager stockManager;
    Producer producer;
    StockMapper mapper;

   @Scheduled(cron = "${ru.otus.spring.cron}")
    void update() {
        try {
            List<StockDto> list = stockManager.getAllStocksOfLastDay();
            stockManager.saveAll(list);
            log.info("Последние значения акций  успешно сохранены: {}", LocalDateTime.now());
            producer.sendForUpdate(mapper.toCacheList(list));
            log.info("Данные для обновления данных в кеше обработчиков сообщений успешно отправлены! {}", LocalDateTime.now());
            stockManager.clearCurrentTable();
            log.info("Очистка таблицы с текущими значениями акции успешно проведена: {}", LocalDateTime.now());
        } catch (Exception e) {
            log.error("Ошибка при реализации работы scheduler", e);
        }
    }

}
