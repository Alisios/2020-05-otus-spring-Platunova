package ru.otus.spring.messagetransfer;

import ru.otus.spring.kafka.StockInfoForUser;

public interface NotifyService {

    void send(StockInfoForUser info);

}
