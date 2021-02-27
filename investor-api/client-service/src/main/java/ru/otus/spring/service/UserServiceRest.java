package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.spring.configs.RestProperties;
import ru.otus.spring.service.dto.User;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceRest implements UserService {

    private final RestTemplate restTemplate;
    private final RestProperties restProperties;

    @Override
    public User save(User user) {
        var headers = new HttpHeaders();
        HttpEntity<User> requestParam = new HttpEntity<>(user, headers);
        ResponseEntity<User> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPortOfUserService())
                                .host(restProperties.getHost())
                                .path(restProperties.getUserInfoServicePath()).toUriString(),
                        HttpMethod.POST,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response.getBody();
        else
            throw new RestException("Невозможно сохранить пользователя. Код: " + response.getStatusCode());

    }

//    @Override
//    public void deleteById(long id) {
//        try {
//            userRepository.deleteById(id);
//        } catch (Exception ex) {
//            throw new ServiceException("Error with deleting user with id " + id, ex);
//        }
//    }

    @Override
    public List<User> getAll() {
        var headers = new HttpHeaders();
        HttpEntity<String> requestParam = new HttpEntity<>(headers);
        ResponseEntity<List<User>> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPortOfUserService())
                                .host(restProperties.getHost())
                                .path(restProperties.getSubsServiceAdminPath()).toUriString(),
                        HttpMethod.GET,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response.getBody();
        else
            throw new RestException("Невозможно получить пользователей. Код: " + response.getStatusCode());
    }

    @Override
    public Optional<User> getById(long id) {
        var headers = new HttpHeaders();
        HttpEntity<String> requestParam = new HttpEntity<>(headers);
        ResponseEntity<Optional<User>> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPortOfUserService())
                                .host(restProperties.getHost())
                                .path(restProperties.getUserInfoServicePath() + id).toUriString(),
                        HttpMethod.GET,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response.getBody();
        else
            throw new RestException("Невозможно получить пользователяи. Код: " + response.getStatusCode());

    }

    @Override
    public List<User> getByName(String name, String surname) {
        var headers = new HttpHeaders();
        HttpEntity<String> requestParam = new HttpEntity<>(headers);
        ResponseEntity<List<User>> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPortOfUserService())
                                .host(restProperties.getHost())
                                .queryParam("name", name)
                                .queryParam("surname", surname)
                                .path(restProperties.getUserInfoServicePath()).toUriString(),
                        HttpMethod.GET,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response.getBody();
        else
            throw new RestException("Невозможно получить пользователя. Код: " + response.getStatusCode());


    }

    @Override
    public Optional<User> getByLogin(String login) {
        var headers = new HttpHeaders();
        HttpEntity<String> requestParam = new HttpEntity<>(headers);
        ResponseEntity<Optional<User>> response =
                restTemplate.exchange(UriComponentsBuilder.newInstance()
                                .scheme(restProperties.getScheme())
                                .port(restProperties.getPortOfUserService())
                                .queryParam("login", login)
                                .host(restProperties.getHost())
                                .path(restProperties.getUserInfoServicePath()).toUriString(),
                        HttpMethod.GET,
                        requestParam,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Ответ: {}, {}", response.getStatusCode(), response.getBody());
        if (response.getStatusCode() == HttpStatus.OK)
            return response.getBody();
        else
            throw new RestException("Невозможно получить пользователя. Код: " + response.getStatusCode());

    }

}
