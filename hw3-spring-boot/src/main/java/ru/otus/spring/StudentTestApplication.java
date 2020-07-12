package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.service.TestService;
import ru.otus.spring.service.TestServiceImpl;

@SpringBootApplication
public class StudentTestApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(StudentTestApplication.class, args);
        var service = context.getBean(TestServiceImpl.class);
        try {
            service.startTest();
        } catch (QuestionDaoException ex) {
            System.out.println("Impossible to handle file for test" + ex.getCause() + ". " + ex.getMessage());
        }
    }
}
