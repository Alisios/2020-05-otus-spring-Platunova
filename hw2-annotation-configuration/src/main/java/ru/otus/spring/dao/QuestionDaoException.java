package ru.otus.spring.dao;

public class QuestionDaoException  extends Exception{

    public QuestionDaoException(String msg) {
        super(msg);
    }

    public QuestionDaoException(Exception ex) {
        super(ex);
    }
}
