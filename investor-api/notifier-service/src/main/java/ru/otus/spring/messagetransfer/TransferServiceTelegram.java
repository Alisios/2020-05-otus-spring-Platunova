package ru.otus.spring.messagetransfer;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.otus.spring.kafka.StockInfoForUser;
import ru.otus.spring.telegramservice.Bot;

/**
 * Сервис, отпарвляющий сообщение пользователю через телеграмм
 */
@Service("telegram-service")
@Slf4j
@Setter
public class TransferServiceTelegram implements TransferService {

    private Bot bot;

//    @Value("${mail-properties.key}")
//    private static String KEY;

    @Autowired
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 500))
    public void sendToUser(StockInfoForUser message) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(message.getTelegram());
            sendMessage.setText(message.getMessageForUser());
            sendMessage.setParseMode("HTML");
            bot.execute(sendMessage);
        } catch (TelegramApiException ex) {
            log.info("Ошибка отправления сообщения пользователю в телеграмме {}\t {}\n", ex.getCause(), ex.getMessage(), ex);
            throw new TransferException("Ошибка отправления сообщения пользователю в телеграмме r" + ex.getCause() + ". ", ex);
        }
    }
}
