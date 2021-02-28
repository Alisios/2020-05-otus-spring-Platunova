package ru.otus.spring.subscriptionmanager;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.otus.spring.common.CrudMessage;
import ru.otus.spring.subscriptionmanager.database.SubscriptionDbService;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionManagerImpl implements SubscriptionManager {

    SubscriptionDbService subscriberDbService;

    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public void handleCrudFromUser(CrudMessage crudMessage) {
        switch (crudMessage.getType().getValue()) {
            case ("save_subscription"):
                subscriberDbService.saveSubscription(crudMessage.getSubscriptionDto());
                break;
            case ("delete_subscription"):
                subscriberDbService.deleteSubscription(crudMessage.getSubscriptionDto().getTicker(), crudMessage.getSubscriptionDto().getTypeEvent());
                break;
            case ("delete_user"):
                subscriberDbService.deleteUser(crudMessage.getSubscriptionDto().getTicker(), crudMessage.getSubscriptionDto().getTypeEvent(), crudMessage.getSubscriptionDto().getUserDto().getUserRealId());
                break;
            default:
                log.error("Неизвестный тип операции с подпиской!");
                break;
        }
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public List<SubscriptionDto> getSubsForAdmin() {
        return subscriberDbService.getAllSubscriptions();
    }


    @Override
    public Optional<SubscriptionDto> getByTickerAndEventType(@NotNull String ticker, @NotNull String eventType) {
        return subscriberDbService.getByTickerAndEventType(ticker, eventType);
    }

}
