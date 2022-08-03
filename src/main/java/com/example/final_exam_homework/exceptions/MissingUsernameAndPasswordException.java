package com.example.final_exam_homework.exceptions;

public class MissingUsernameAndPasswordException extends RuntimeException{

    public MissingUsernameAndPasswordException(String message) {
        super(message);
    }
}
