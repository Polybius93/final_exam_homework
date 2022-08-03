package com.example.final_exam_homework.exceptions;

public class UsernameAlreadyInUseException extends RuntimeException{

    public UsernameAlreadyInUseException(String message) {
        super(message);
    }
}
