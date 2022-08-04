package com.example.final_exam_homework.dtos;

public class BidWithUsernameDTO {

    private Long value;
    private String username;

    public BidWithUsernameDTO() {
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
