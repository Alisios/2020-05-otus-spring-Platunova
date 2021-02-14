package ru.otus.spring.telegramservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import ru.otus.spring.configuration.BotProperties;

/**
 * реализует telegram api, настройка бота
 */

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Bot extends TelegramLongPollingBot {
    BotProperties properties;

    @Override
    public void onUpdateReceived(Update update) {
        log.info("{}, {}", update.getMessage(), update.getUpdateId());
        if (update.hasMessage()) {
            Message message = update.getMessage();
            log.info("new message in update {}", message.getText());
        }
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }

    @Override
    public String getBotToken() {
        return properties.getTokien();
    }

    public void botConnect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            log.info("TelegramAPI started. Looking for messages");
        } catch (TelegramApiRequestException e) {
            log.error("Can't Connect. Pause " + properties.getReconnectPause() / 1000 + "sec and try again. Error: " + e.getMessage() + "\n" + e.getApiResponse() + "\n" + e.getCause() + "\n");
            e.printStackTrace();
            try {
                Thread.sleep(properties.getReconnectPause());
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
                log.error("Interrupted Exception exception: {}\n{}", e1.getCause(), e1.getStackTrace());
                return;
            }
            botConnect();
        }
    }
}