package ru.otus.spring.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тесты проверяют: ")
class ParserCsvTest {


     @Test
    @DisplayName("корректный парсинг и маппинг csv")
    void correctParsingAndMappingOfCsv() throws IOException {
        URL path = Thread.currentThread().getContextClassLoader().getResource("questions-and-answers.csv");// "";
        Parser parser = new ParserCsv(Objects.requireNonNull(path).openStream());//context.getBean(Parser.class);
        List<Question> parseQuestions = parser.parseQuestions();
        assertThat(parseQuestions).isNotEmpty();
        parseQuestions.forEach(answer -> {
            assertThat(answer.getListOfAnswers()).isNotEmpty();
            assertThat(answer.getListOfAnswers().size()).isEqualTo(3);
        });
    }

    @Test
    @DisplayName("бросание исключения при некорректной заполненности сsv")
    void correctThrowingOfExceptionOfIncorrectDateInCsv() throws IOException {
        URL path = Thread.currentThread().getContextClassLoader().getResource("questions-and-answersError.csv");// "";
        Parser parser = new ParserCsv(path.openStream());//context.getBean(Parser.class);
        assertThrows(RuntimeException.class, parser::parseQuestions);
    }

    @Test
    @DisplayName("бросание исключения при отсуствии файла с данным имененм")
    void correctThrowingOfExceptionOfNullFile() throws IOException {
        URL path = Thread.currentThread().getContextClassLoader().getResource("questions-and-answersError5678.csv");// "";
        assertThrows(Exception.class, () ->{
            Parser parser = new ParserCsv(path.openStream());
            parser.parseQuestions();
        });
    }


}