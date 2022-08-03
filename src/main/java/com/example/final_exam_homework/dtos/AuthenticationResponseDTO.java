package com.example.final_exam_homework.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponseDTO {

    private final String status;
    private final String jwt;

    @JsonProperty(value = "greenbay_dollars")
    private Long greenBayDollars;

    public AuthenticationResponseDTO(String jwt) {
        this.jwt = jwt;
        this.status = "ok";
    }

    public String getStatus() {
        return status;
    }

    public String getJwt() {
        return jwt;
    }

    public Long getGreenBayDollars() {
        return greenBayDollars;
    }

    public void setGreenBayDollars(Long greenBayDollars) {
        this.greenBayDollars = greenBayDollars;
    }
}
