package ru.otus.spring.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.dao.QuestionDaoImpl;
import ru.otus.spring.helper.Parser;
import ru.otus.spring.helper.ParserCsv;
import ru.otus.spring.service.IOService;
import ru.otus.spring.service.IOServiceImpl;

import java.io.PrintStream;
import java.util.Scanner;

@Configuration
@PropertySource("classpath:application.properties")
public class ResourceConfig {

    final private PrintStream out = System.out;
    final private Scanner in = new Scanner(System.in);


    @Bean
    Parser parserCSV( @Value("${service.path}") String resourceName) {
        return new ParserCsv(resourceName);
    }

    @Bean
    QuestionDao questionDao(Parser parser){
        return new QuestionDaoImpl(parser);
    }

    @Bean
    IOService outputServiceImpl(){
        return new IOServiceImpl(out, in);
    }
}
