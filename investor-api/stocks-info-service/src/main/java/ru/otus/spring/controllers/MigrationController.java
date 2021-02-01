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
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.configs.AppProps;

import static ru.otus.spring.configs.JobConfig.INPUT_FILE_NAME;

@RestController
@Tag(name = "Миграция исторческих данных по компаниям")
//http://localhost:7091/swagger-ui.html
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Ошибка на стороне клиента",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                        mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                        mediaType = "application/json"))
})
@ResponseStatus(code = HttpStatus.OK)
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/migration")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MigrationController {

    AppProps appProps;
    Job importUserJob;
    JobLauncher jobLauncher;

    @Operation(summary = "Запуск миграции даннных")
    @GetMapping({"{fileName}"})
    @ApiResponse(responseCode = "200", description = "Миграция запущена")
    public void startMapping(
            @Parameter(description = "Название файла для миграции в формате НазваниеКомпании_ТипКомпании.csv. " +
                    "Файл должен находиться в папке resources",
                    required = true, example = "Аэрофлот_авиаперевозки.csv")
            @PathVariable("fileName") String fileName) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        appProps.setInputFile("stocks-info-service/src/main/resources/historyInfo/" + fileName);
        JobExecution execution = jobLauncher.run(importUserJob, new JobParametersBuilder()
                .addString(INPUT_FILE_NAME, appProps.getInputFile())
                .toJobParameters());
        log.info(String.valueOf(execution));
    }

}
