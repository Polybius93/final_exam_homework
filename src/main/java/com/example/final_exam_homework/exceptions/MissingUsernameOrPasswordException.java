package com.example.final_exam_homework.exceptions;

public class MissingUsernameOrPasswordException extends RuntimeException{

    public MissingUsernameOrPasswordException(String message) {
        super(message);
    }
}
