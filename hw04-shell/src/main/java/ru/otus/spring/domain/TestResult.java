package ru.otus.spring.domain;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TestResult {
    User user;

    int score;

    String res;

    @Override
    public String toString() {
        return user.getName() + ' ' + user.getSurname() + ":\n" + res;
    }
}
