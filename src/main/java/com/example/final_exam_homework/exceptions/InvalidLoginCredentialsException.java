package com.example.final_exam_homework.exceptions;

public class InvalidLoginCredentialsException extends RuntimeException{

    public InvalidLoginCredentialsException(String message) {
        super(message);
    }
}
