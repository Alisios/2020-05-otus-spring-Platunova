package ru.otus.spring.messagetransfer;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.spring.kafka.StockInfoForUser;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NotifyServiceImpl implements NotifyService {

    @Qualifier("email_service")
    TransferService transferServiceEmail;

    @Qualifier("telegram-service")
    TransferService transferServiceTm;

    @Autowired
    NotifyServiceImpl(
            @Qualifier("email_service") TransferService transferService,
            @Qualifier("telegram-service") TransferService transferServiceTm) {
        this.transferServiceEmail = transferService;
        this.transferServiceTm = transferServiceTm;
    }

    @Override
    public void send(StockInfoForUser info) {
        if (info.getEmail() != null && !info.getEmail().isEmpty())
            transferServiceEmail.sendToUser(info);
        if (info.getTelegram() != null && !info.getTelegram().isEmpty())
            transferServiceTm.sendToUser(info);
    }
}
