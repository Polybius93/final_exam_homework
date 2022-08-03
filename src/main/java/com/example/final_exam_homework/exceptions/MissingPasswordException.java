package com.example.final_exam_homework.exceptions;

public class MissingPasswordException extends RuntimeException{

    public MissingPasswordException(String message) {
        super(message);
    }
}
