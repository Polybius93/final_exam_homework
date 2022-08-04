package com.example.final_exam_homework.exceptions;

public class ItemIsAlreadySoldException extends RuntimeException{

    public ItemIsAlreadySoldException(String message) {
        super(message);
    }
}
