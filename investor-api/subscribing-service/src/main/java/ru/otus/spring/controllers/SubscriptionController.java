package ru.otus.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.subscriptionmanager.SubscriptionDbServiceException;
import ru.otus.spring.subscriptionmanager.database.SubscriptionDbService;
import ru.otus.spring.subscriptionmanager.database.dto.EntityMapper;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDto;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDtoFromUser;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Контроллер информации о подписках")
//http://localhost:2090/swagger-ui.html
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Ошибка на стороне клиента"),
        @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера")
})
@ResponseStatus(code = HttpStatus.OK)
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubscriptionController {

    SubscriptionDbService stockServiceDb;
    EntityMapper mapper;


    @Operation(summary = "Получение информации о всех подписках")
    @GetMapping({"/subscriptions"})
    @ApiResponse(responseCode = "200", description = "Успешно",
            content = @Content(schema = @Schema(implementation = SubscriptionDto.class),
                    mediaType = "application/json"))
    public List<SubscriptionDto> getAll() {
        return stockServiceDb.getAllSubscriptions();
    }

    @Operation(summary = "Добавление подписки")
    @PostMapping({"/subscription"})
    @ApiResponse(responseCode = "201",
            description = "Информация по компании сохранена")
    @ResponseStatus(code = HttpStatus.CREATED)
    public SubscriptionDto add(@RequestBody
                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                       description = "Информация по подписке для записи в БД", required = true,
                                       content = @Content(schema = @Schema(implementation = SubscriptionDtoFromUser.class),
                                               mediaType = "application/json"))
                                       SubscriptionDtoFromUser stockDto) {
        return mapper.subToDto(stockServiceDb.saveSubscription(stockDto));
    }

    @Operation(summary = "Удаление подписки")
    @DeleteMapping({"/subscription/{ticker}/{event}/user/{id}"})
    @ApiResponse(responseCode = "200",
            description = "Подписка удалена")
    public String delete(@Parameter(description = "Уникальный идентификатор компании на бирже", required = true, example = "sber")
                         @PathVariable("ticker") String ticker,
                         @Parameter(description = "Тип события", required = true, example = "event_1")
                         @PathVariable("event") String event,
                         @Parameter(description = "Id", required = true, example = "")
                         @PathVariable("id") String id) {
        return stockServiceDb.deleteUser(ticker, event, id).toString();
    }


    @Operation(summary = "Удаление подписки по ticker и event")
    @DeleteMapping({"/subscription/{ticker}/{event}"})
    @ApiResponse(responseCode = "200",
            description = "Подписка удалена")
    public String deleteById(@Parameter(description = "Уникальный идентификатор компании на бирже", required = true, example = "sber")
                             @PathVariable("ticker") String ticker,
                             @Parameter(description = "Тип события", required = true, example = "event_1")
                             @PathVariable("event") String event) {
        return stockServiceDb.deleteSubscription(ticker, event).toString();
    }


    @Operation(summary = "Получение подписки по тикеру и типу события")
    @GetMapping(value = {"/subscriptions/{ticker}/{event}"})
    @ApiResponse(responseCode = "200", description = "Запись найдена",
            content = @Content(schema = @Schema(implementation = SubscriptionDto.class),
                    mediaType = "application/json"))
    public SubscriptionDto getInfoByTickerAndEvent(
            @Parameter(description = "Уникальный идентификатор компании на бирже", required = true, example = "sber")
            @PathVariable("ticker") String ticker,
            @Parameter(description = "Тип события", required = true, example = "event_1")
            @PathVariable("event") String event) {
        return stockServiceDb.getByTickerAndEventType(ticker, event).orElseThrow(() -> new SubscriptionDbServiceException("Информация не найдена"));
    }

}
