package com.example.final_exam_homework.exceptions;

public class InvalidPasswordLengthException extends RuntimeException{

    public InvalidPasswordLengthException(String message) {
        super(message);
    }
}
