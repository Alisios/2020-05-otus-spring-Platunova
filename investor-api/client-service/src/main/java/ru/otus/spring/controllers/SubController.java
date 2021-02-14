package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.service.SubService;
import ru.otus.spring.service.dto.SubscriptionDto;
import ru.otus.spring.service.dto.SubscriptionDtoFromUser;
import ru.otus.spring.service.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
@RequiredArgsConstructor
@Slf4j
public class SubController {

    private final SubService subService;

    @PostMapping(path = "/subs")
    public ResponseEntity<String> saveBook(@RequestBody SubscriptionDtoFromUser dto) {
        return ResponseEntity.ok().body(subService.save(dto).getBody().getId().toString());
    }

    @GetMapping("/subs/list")
    public ResponseEntity<List<SubscriptionDto>> list() {
        return subService.getAllSubs();
    }

    @GetMapping("/sub/info/{ticker}/{event}")
    public ResponseEntity<SubscriptionDto> showBookById(@PathVariable("ticker") String ticker, @PathVariable("event") String event) {
        return subService.getBTickerAndEvent(ticker, event);
    }

    @GetMapping("/sub/{ticker}/{event}/users/list")
    public ResponseEntity<List<UserDto>> showBookWithComments(@PathVariable String ticker, @PathVariable String event, Model model) {
        return ResponseEntity.ok().body(new ArrayList<>(Objects.requireNonNull(subService.getBTickerAndEvent(ticker, event).getBody()).getUsers()));
    }

    @DeleteMapping("/sub/{ticker}/{event}")
    public ResponseEntity<String> deleteSub(@PathVariable String ticker, @PathVariable String event) {
        return subService.deleteSub(ticker, event);
    }

    @DeleteMapping("/sub/{ticker}/{event}/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String ticker, @PathVariable String event, @PathVariable String id) {
        return subService.deleteUser(ticker, event, id);
    }

}
