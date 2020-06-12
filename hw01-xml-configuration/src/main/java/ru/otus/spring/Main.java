package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.service.OutputService;

//java -jar  "./hw01-xml-configuration/target/hw01-xml-configuration-jar-with-dependencies.jar" из папки 2020-05...

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        OutputService outputService = context.getBean(OutputService.class);
        try {
            outputService.showQuestions();
        }catch (QuestionDaoException ex){
            System.out.println("Impossible to handle file"+ ex.getCause()+". "+ex.getMessage());
        }
        context.close();
    }
}
