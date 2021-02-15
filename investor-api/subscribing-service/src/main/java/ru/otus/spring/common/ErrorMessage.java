package ru.otus.spring.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.subscriptionmanager.database.dto.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private Throwable ex;

    private String info;

    private String message;
}
