package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;

import java.io.PrintStream;
import java.util.Scanner;

@RequiredArgsConstructor
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
