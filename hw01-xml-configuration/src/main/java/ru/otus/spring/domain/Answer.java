package ru.otus.spring.domain;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Answer {
    private String contentOfPossibleAnswer;
}
