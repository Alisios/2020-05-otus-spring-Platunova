package ru.otus.spring.stockinfoservice.controllers;

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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.stockinfoservice.database.StockServiceDb;
import ru.otus.spring.stockinfoservice.database.dto.StockDto;
import ru.otus.spring.stockinfoservice.database.dto.StockDtoToUser;
import ru.otus.spring.stockinfoservice.database.dto.StockMapper;
import ru.otus.spring.stockinfoservice.database.entity.StockEntity;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Контроллер-архив информации по компаниям")
//http://localhost:4011/swagger-ui.html
@ResponseStatus(code = HttpStatus.OK)
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StockController {

    StockServiceDb stockServiceDb;
    StockMapper mapper;
    Comparator<StockEntity> comparator = Comparator.comparing(StockEntity::getDate).reversed();

    @Operation(summary = "Получение информации о компании по id")
    @GetMapping({"/{id}"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запись найдена",
                    content = @Content(schema = @Schema(implementation = StockDtoToUser.class),
                            mediaType = "application/json"))})
    public StockDtoToUser getById(@PathVariable("id") String id) {
        return mapper.mapForUser(stockServiceDb.getById(id));
    }

    @Operation(summary = "Получение информации обо всех компаниях")
    @GetMapping({"/stocks"})
    @ApiResponse(responseCode = "200", description = "Успешно",
            content = @Content(schema = @Schema(implementation = StockEntity.class),
                    mediaType = "application/json"))
    public List<StockEntity> getAll() {
        return stockServiceDb.getAllStocks();
    }

    @Operation(summary = "Добавление информации о компании")
    @PostMapping
    @ApiResponse(responseCode = "201",
            description = "Информация по компании сохранена")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Информация по компании для записи в БД", required = true,
                            content = @Content(schema = @Schema(implementation = StockDto.class),
                                    mediaType = "application/json"))
                            StockDto stockDto) {
        stockServiceDb.addStock(stockDto);
    }


    @Operation(summary = "Получени информации по компании за выбранный интервал времени")
    @GetMapping({"/interval/{ticker}/{startTime}/{endTime}"})
    @ApiResponse(responseCode = "200", description = "Запись найдена",
            content = @Content(schema = @Schema(implementation = StockDtoToUser.class),
                    mediaType = "application/json"))
    public List<StockDtoToUser> getByInterval(
            @Parameter(description = "Информация по акции для записи в БД", required = true, example = "2020-10-31T01:30:00.238Z")
            @PathVariable("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "Информация по акции для записи в БД", required = true, example = "2021-01-29T01:30:00.238Z")
            @PathVariable("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "Ticker", required = true, example = "SBER")
            @PathVariable("ticker") String ticker) {
        return stockServiceDb.getByInterval(ticker, startTime, endTime)
                .stream()
                .sorted(comparator)
                .map(mapper::mapForUser)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получение последней информации по ticker компании")
    @GetMapping({"/ticker/{ticker}"})
    @ApiResponse(responseCode = "200", description = "Запись найдена",
            content = @Content(schema = @Schema(implementation = StockDtoToUser.class),
                    mediaType = "application/json"))
    public StockDtoToUser getLastInfoByTicker(
            @Parameter(description = "Ticker", required = true, example = "SBER")
            @PathVariable("ticker") String ticker) {
        return mapper.mapForUser(stockServiceDb.getLastInfoByTicker(ticker));
    }

    @Operation(summary = "Получение последней информации по типу компании")
    @GetMapping(value = {"/type/{type}"})
    @ApiResponse(responseCode = "200", description = "Запись найдена",
            content = @Content(schema = @Schema(implementation = StockDtoToUser.class),
                    mediaType = "application/json"))
    public List<StockDtoToUser> getLastInfoByType(
            @Parameter(description = "Тип (Банк, IT, нефть-и-газ и т.д)", required = true, example = "Банки")
            @PathVariable("type") String type) {
        return stockServiceDb.getByType(type)
                .stream()
                .map(mapper::mapForUser)
                .collect(Collectors.toList());
    }

}
