package ru.otus.spring.dispatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.spring.configuration.RestProperties;
import ru.otus.spring.informationservice.StockInfo;

/**
 * сервис принимает информацию с биржи по рест
 */

@Service("infoDispatcherDefault")
@RequiredArgsConstructor
@Slf4j
public class InfoDispatcherDefault implements InfoDispatcher {

    private final RestTemplate restTemplate;
    private final RestProperties restProperties;

    @Override
    public StockInfo getInfo() {
        var headers = new HttpHeaders();
        HttpEntity<String> requestParam = new HttpEntity<>(headers);
        ResponseEntity<StockInfo> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .host(restProperties.getHost())
                                .path(restProperties.getPathInfoSource2()).toUriString(),
                        HttpMethod.GET,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response.getBody();
        else
            throw new RuntimeException("Невозможно получить ответ с биржи. Код : " + response.getStatusCode());
    }

}
