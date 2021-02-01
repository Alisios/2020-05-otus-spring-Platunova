package ru.otus.spring.configs;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI switchSwagger(){
        return new OpenAPI()
                .info(new Info()
                .title("Сервис исторических данных по акциям")
                .description("")
                .version(""))
                .externalDocs(new ExternalDocumentation()
                .description("")
                .url(""));
    }
}
