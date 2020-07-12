package ru.otus.spring.service;

import lombok.AllArgsConstructor;

import java.io.PrintStream;
import java.util.Scanner;

@AllArgsConstructor
public class IOServiceImpl implements IOService {

    private final PrintStream outPut;
    private final Scanner in;

    @Override
    public void outputMessage(String s) {
        outPut.println(s);
    }

    @Override
    public String inputMessage() {
        return in.nextLine();
    }
}
