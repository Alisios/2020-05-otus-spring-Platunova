package ru.otus.spring.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.spring.configs.RestProperties;
import ru.otus.spring.service.dto.SubscriptionDto;
import ru.otus.spring.service.dto.SubscriptionDtoFromUser;


import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubServiceRest implements SubService {

    private final RestTemplate restTemplate;
    private final RestProperties restProperties;


    @Override
    public ResponseEntity<SubscriptionDto> save(SubscriptionDtoFromUser user) {
        user.setId(UUID.randomUUID());
        user.getUserDto().setId(UUID.randomUUID());
        var headers = new HttpHeaders();
        HttpEntity<SubscriptionDtoFromUser> requestParam = new HttpEntity<>(user, headers);
        ResponseEntity<SubscriptionDto> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPort2())
                                .host(restProperties.getHost())
                                .path(restProperties.getSubsServicePath()).toUriString(),
                        HttpMethod.POST,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.CREATED)
            return response;
        else
            throw new RestException("Невозможно сохранить пользователя. Код: " + response.getStatusCode());

    }

    @Override
    public ResponseEntity<List<SubscriptionDto>> getAllSubs() {
        var headers = new HttpHeaders();
        HttpEntity<?> requestParam = new HttpEntity<>(headers);
        ResponseEntity<List<SubscriptionDto>> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPort2())
                                .host(restProperties.getHost())
                                .path(restProperties.getSubsServiceAdminPath()).toUriString(),
                        HttpMethod.GET,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response;
        else
            throw new RestException("Невозможно сохранить пользователя. Код: " + response.getStatusCode());
    }

    @Override
    public ResponseEntity<SubscriptionDto> getById(UUID id) {
        var headers = new HttpHeaders();
        HttpEntity<?> requestParam = new HttpEntity<>(headers);
        ResponseEntity<SubscriptionDto> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPort2())
                                .host(restProperties.getHost())
                                .path(restProperties.getSubsServicePath() + "/" + id).toUriString(),
                        HttpMethod.GET,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response;
        else
            throw new RestException("Невозможно сохранить пользователя. Код: " + response.getStatusCode());
    }

    @Override
    public ResponseEntity<SubscriptionDto> getBTickerAndEvent(String ticker, String event) {
        var headers = new HttpHeaders();
        HttpEntity<?> requestParam = new HttpEntity<>(headers);
        ResponseEntity<SubscriptionDto> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPort2())
                                .host(restProperties.getHost())
                                .path(restProperties.getSubsServiceAdminPath() + "/" + ticker + "/" + event).toUriString(),
                        HttpMethod.GET,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response;
        else
            throw new RestException("Невозможно сохранить пользователя. Код: " + response.getStatusCode());
    }

    @Override
    public ResponseEntity<String> deleteSub(String ticker, String event) {
        var headers = new HttpHeaders();
        HttpEntity<?> requestParam = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPort2())
                                .host(restProperties.getHost())
                                .path(restProperties.getSubsServicePath() + "/" + ticker + "/" + event).toUriString(),
                        HttpMethod.DELETE,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response;
        else
            throw new RestException("Невозможно сохранить пользователя. Код: " + response.getStatusCode());
    }

    @Override
    public ResponseEntity<String> deleteUser(String ticker, String event, String id) {
        var headers = new HttpHeaders();
        HttpEntity<?> requestParam = new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPort2())
                                .host(restProperties.getHost())
                                .path(restProperties.getSubsServicePath() + "/" + ticker + "/" + event + restProperties.getSubsUserServicePath() + id).toUriString(),
                        HttpMethod.DELETE,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response;
        else
            throw new RestException("Невозможно сохранить пользователя. Код: " + response.getStatusCode());
    }
}
