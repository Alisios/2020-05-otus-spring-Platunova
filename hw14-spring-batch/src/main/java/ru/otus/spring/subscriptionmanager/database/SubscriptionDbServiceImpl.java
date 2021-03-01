package ru.otus.spring.subscriptionmanager.database;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.subscriptionmanager.database.dto.EntityMapper;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDto;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDtoFromUser;
import ru.otus.spring.subscriptionmanager.database.entities.Subscription;
import ru.otus.spring.subscriptionmanager.database.entities.User;
import ru.otus.spring.subscriptionmanager.database.repository.SubscriptionRepository;
import ru.otus.spring.subscriptionmanager.database.repository.UserRepository;
import ru.otus.spring.subscriptionmanager.SubscriptionDbServiceException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionDbServiceImpl implements SubscriptionDbService {

    UserRepository userRepository;
    SubscriptionRepository subscriptionRepository;
    EntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionDto> getByTickerAndEventType(String ticker, String eventType) {
        try {
            return Optional.of(mapper.subToDto(subscriptionRepository.findByTickerAndTypeEvent(ticker, eventType).get()));
        } catch (RuntimeException e) {
            log.error("Ошибка при получении подписки по типу и тикеру");
            throw new SubscriptionDbServiceException("Ошибка при получении подписки по типу и тикеру", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionDto> getAllSubscriptions() {
        try {
            return subscriptionRepository.findAll().stream().map(mapper::subToDto).collect(Collectors.toList());
        } catch (RuntimeException e) {
            log.error("Ошибка при получении всех подписок");
            throw new SubscriptionDbServiceException("Ошибка при получении всех подписок", e);
        }
    }

    @Override
    @Transactional
    public Subscription saveSubscription(SubscriptionDtoFromUser subscriptionDto) {
        try {
            Optional<Subscription> sub = subscriptionRepository.findByTickerAndTypeEvent(subscriptionDto.getTicker(), subscriptionDto.getTypeEvent());
            User userSaved;
            Subscription subNew;
            if (!sub.isPresent()) {
                subNew = mapper.fromSubUserDtoToSub(subscriptionDto);
                subNew.setUsers(new HashSet<>());
                subNew.setId(UUID.randomUUID());
                subscriptionDto.getUserDto().setId(UUID.randomUUID());
                userSaved = userRepository.save(mapper.dtoToUser(subscriptionDto.getUserDto()));
            } else {
                subNew = sub.get();
                Optional<User> user = userRepository.findByUser_id(subscriptionDto.getUserDto().getUserRealId());
                userSaved = user.orElseGet(() -> {
                    subscriptionDto.getUserDto().setId(UUID.randomUUID());
                    return userRepository.save(mapper.dtoToUser(subscriptionDto.getUserDto()));
                });
            }
            subNew.addUser(userSaved);
            return subscriptionRepository.save(subNew);
        } catch (RuntimeException ex) {
            log.error("Ошибка при сохранении подписки", ex);
            throw new SubscriptionDbServiceException("Ошибка при сохранении подписки", ex);
        }
    }


    @Override
    @Transactional
    public UUID deleteSubscription(String ticker, String event) {
        try {
            Subscription sub = subscriptionRepository
                    .findByTickerAndTypeEvent(ticker, event)
                    .orElseThrow(() -> new SubscriptionDbServiceException("Ошибка при удалении подписки! Такой подписки не существует!"));
            subscriptionRepository.deleteById(sub.getId());
            return sub.getId();
        } catch (RuntimeException ex) {
            log.error("Ошибка при удалении подписки", ex);
            throw new SubscriptionDbServiceException("Ошибка при удалении подписки", ex);
        }
    }

    @Override
    @Transactional
    public UUID deleteUser(String ticker, String event, String id) {
        try {
            Subscription sub = subscriptionRepository
                    .findByTickerAndTypeEvent(ticker, event)
                    .orElseThrow(() -> new SubscriptionDbServiceException("Ошибка при удалении подписки! Такой подписки не существует!"));
            User user = userRepository.findByUser_id(id).orElseThrow(() -> new SubscriptionDbServiceException("Ошибка при удалении подписчика! Такого подписчика не существует!"));
            sub.removeUser(user);
            subscriptionRepository.save(sub);
            return user.getId();
        } catch (RuntimeException ex) {
            log.error("Ошибка при удалении подписки", ex);
            throw new SubscriptionDbServiceException("Ошибка при удалении подписки", ex);
        }
    }


}


