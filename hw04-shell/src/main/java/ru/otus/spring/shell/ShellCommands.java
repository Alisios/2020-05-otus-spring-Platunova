package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;
import ru.otus.spring.service.IOService;
import ru.otus.spring.service.TestHandler;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {
    final private TestHandler testHandler;
    final private IOService ioService;
    private User user;
    private TestResult testResult;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public void login() {
        user = testHandler.getUserFromInput();
    }

    @ShellMethod(value = "Start test", key = {"t", "test"})
    @ShellMethodAvailability(value = "isPublishEventCommandAvailable")
    public void startTest() {
        try {
            testResult = testHandler.executeTest(user);
        } catch (QuestionDaoException ex) {
            System.out.println("Impossible to handle file for test" + ex.getCause() + ". " + ex.getMessage());
        }
    }

    @ShellMethod(value = "Show results", key = {"res", "results"})
    @ShellMethodAvailability(value = "isTestedTrue")
    public void showResult() {
        ioService.outputMessage(testHandler.printResultsOfTest(testResult));
        user = null;
        testResult = null;
    }

    private Availability isPublishEventCommandAvailable() {
        return user == null
                ? Availability.unavailable("Чтобы начать тест зайдите в систему при помощи команды login")
                : Availability.available();
    }


    private Availability isTestedTrue() {
        return (user == null || !user.getIsTested())
                ? Availability.unavailable("Чтобы получить результаты тесты нужно залогиниться в систему при помощи команды login и пройти тест при помощу команды test")
                : Availability.available();
    }

}
