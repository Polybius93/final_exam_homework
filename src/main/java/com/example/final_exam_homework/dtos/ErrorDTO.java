package com.example.final_exam_homework.dtos;

public class ErrorDTO {

    private String error;

    public ErrorDTO(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
