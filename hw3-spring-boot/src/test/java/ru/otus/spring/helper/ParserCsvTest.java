package ru.otus.spring.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.Localizer;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Тесты проверяют: ")
class ParserCsvTest {

    private final Localizer localizer = mock(Localizer.class);

     @Test
    @DisplayName("корректный парсинг и маппинг csv")
    void correctParsingAndMappingOfCsv() throws IOException {
       when(localizer.getLocalizedQuestionFile()).thenReturn( "questions-and-answers.csv");
        Parser parser = new ParserCsv(localizer);
        List<Question> parseQuestions = parser.parseQuestions();
        assertThat(parseQuestions).isNotEmpty();
        parseQuestions.forEach(answer -> {
            assertThat(answer.getListOfAnswers()).isNotEmpty();
            assertThat(answer.getListOfAnswers().size()).isEqualTo(3);
        });
    }

    @Test
    @DisplayName("бросание исключения при некорректной заполненности сsv")
    void correctThrowingOfExceptionOfIncorrectDateInCsv() {
        when(localizer.getLocalizedQuestionFile()).thenReturn( "questions-and-answersError.csv");
        Parser parser = new ParserCsv(localizer);
        assertThrows(RuntimeException.class, parser::parseQuestions);
    }

    @Test
    @DisplayName("бросание исключения при отсуствии файла с данным имененм")
    void correctThrowingOfExceptionOfNullFile() {
        when(localizer.getLocalizedQuestionFile()).thenReturn("questions-and-answersError5678.csv");
        assertThrows(Exception.class, () ->{
            Parser parser = new ParserCsv(localizer);
            parser.parseQuestions();
        });
    }


}