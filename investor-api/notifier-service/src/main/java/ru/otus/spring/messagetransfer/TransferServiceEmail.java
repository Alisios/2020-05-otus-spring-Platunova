package ru.otus.spring.messagetransfer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import ru.otus.spring.configuration.MailProperties;
import ru.otus.spring.kafka.StockInfoForUser;
import ru.otus.spring.statisticsservice.StatElement;
import ru.otus.spring.statisticsservice.StatRepository;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Сервис, отправляющий сообщение пользователю через e-mail
 */

@Service("email_service")
@RequiredArgsConstructor
@Slf4j
public class TransferServiceEmail implements TransferService {

    private final JavaMailSender emailSender;
    private final MailProperties mailProperties;
    private final StatRepository statRepository;
    
    @Value("${mail-properties.key}")
    private static String KEY;
    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 500))
    public void sendToUser(StockInfoForUser info) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailProperties.getServer_email());
            message.setTo(info.getEmail());
            message.setSubject("Инвестиции апи");
            message.setText(info.getMessageForUser());
            emailSender.send(message);
            count.incrementAndGet();
            statRepository.findById(KEY).get().setMailCount(count.get());
        } catch (Exception ex) {
            log.error("Ошибка отправки сообщения пользователю по почте", ex);
            throw new TransferException("Ошибка отправки сообщения пользователю по почте", ex);
        }
    }
}
