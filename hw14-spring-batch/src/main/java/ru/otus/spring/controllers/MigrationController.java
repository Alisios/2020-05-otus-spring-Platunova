package ru.otus.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.configuration.AppProps;


@RestController
@Tag(name = "Миграция исторческих данных по компаниям")
//http://localhost:2090/swagger-ui.html

@ResponseStatus(code = HttpStatus.OK)
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MigrationController {

    Job importSubJob;
    Job importUserJob;
    JobLauncher jobLauncher;

    @Operation(summary = "Запуск миграции даннных")
    @GetMapping("/migration")
    @ApiResponse(responseCode = "200", description = "Миграция запущена")
    public void startMapping() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobExecution execution = jobLauncher.run(importSubJob, new JobParameters());
        log.info(String.valueOf(execution));

        JobExecution userExecution = jobLauncher.run(importUserJob, new JobParameters());
        log.info(String.valueOf(userExecution));
    }

}
