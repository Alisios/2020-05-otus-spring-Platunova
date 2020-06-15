package ru.otus.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.spring.dao.QuestionDaoException;

import ru.otus.spring.service.TestService;
import ru.otus.spring.service.TestServiceImpl;

//java -jar  "./hw2-annotation-configuration/target/hw2-annotation-configuration-jar-with-dependencies.jar" из папки 2020-05...
@ComponentScan("ru.otus")
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        TestService service = context.getBean(TestServiceImpl.class);
        try {
            service.startTest();
        } catch (QuestionDaoException ex) {
            System.out.println("Impossible to handle file" + ex.getCause() + ". " + ex.getMessage());
        }
        context.close();
    }
}
