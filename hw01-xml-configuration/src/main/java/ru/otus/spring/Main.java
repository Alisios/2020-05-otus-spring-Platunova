package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.QuestionService;

import java.util.List;

//java -jar  "./hw01-xml-configuration/target/hw01-xml-configuration-jar-with-dependencies.jar" из папки 2020-05...

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        QuestionService service = context.getBean(QuestionService.class);
        List<Question> allQuestionsAndAnswers = service.getAll();
        allQuestionsAndAnswers.forEach((question) -> {
            System.out.println(question.getId() + ") " +
                    question.getContentOfQuestion());
            question.getListOfAnswers().forEach(answer -> System.out.println("- " + answer.getContentOfPossibleAnswer()));
        });

        context.close();
    }
}
