package ru.otus.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sectorservice.SectorDbService;
import ru.otus.spring.sectorservice.entity.StockType;

import java.util.List;


@RestController
@Tag(name = "Контроллер по сектору ценных бумаг")
//http://localhost:8011/swagger-ui.html
@ResponseStatus(code = HttpStatus.OK)
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Controller {

    SectorDbService service;

    @Operation(summary = "Получение всех секторов компаний ценных бумаг")
    @GetMapping({"/sectors"})
    @ApiResponse(responseCode = "200", description = "Успешно",
            content = @Content(schema = @Schema(implementation = StockType.class),
                    mediaType = "application/json"))
    public List<StockType> getAll() {
        return service.getAll();
    }


    @Operation(summary = "Добавление информации о типе")
    @PostMapping({"/sectors"})
    @ApiResponse(responseCode = "201",
            description = "Информация по типу сохранена")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Сектор компании для записи в БД", required = true,
                            content = @Content(schema = @Schema(implementation = StockType.class),
                                    mediaType = "application/json"))
                            StockType stockType) {
        service.add(stockType);
    }

    @Operation(summary = "Удаление информации про сектор компании ценных бумаг")
    @DeleteMapping({"/sectors/{id}"})
    @ApiResponse(responseCode = "200",
            description = "Сектор компании ценной бумаги удален")
    public void add(@PathVariable String id) {
        service.delete(id);
    }
}
