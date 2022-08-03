package com.example.final_exam_homework.exceptions;

public class MissingUsernameException extends RuntimeException{

    public MissingUsernameException(String message) {
        super(message);
    }
}
