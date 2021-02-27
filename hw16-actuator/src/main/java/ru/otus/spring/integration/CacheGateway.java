package ru.otus.spring.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import ru.otus.spring.stockinfoservice.database.dto.CacheStockInfo;


import java.util.List;

@MessagingGateway
public interface CacheGateway {

    @Gateway(requestChannel = "cacheInfoChannel")
    void sendForUpdate(@Payload List<CacheStockInfo> info);
}
