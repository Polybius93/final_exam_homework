package com.example.final_exam_homework.dtos;

public class UserRegistrationResponseDTO {

    private String username;
    private Long id;

    public UserRegistrationResponseDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
