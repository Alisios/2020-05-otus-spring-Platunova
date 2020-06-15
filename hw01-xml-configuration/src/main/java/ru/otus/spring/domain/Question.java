package ru.otus.spring.domain;

import lombok.*;

import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Question {

    private long id;
    private String contentOfQuestion;
    private List<Answer> listOfAnswers;
}
