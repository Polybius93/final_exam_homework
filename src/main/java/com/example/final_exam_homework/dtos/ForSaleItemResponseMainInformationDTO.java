package com.example.final_exam_homework.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForSaleItemResponseMainInformationDTO {

    protected String name;

    @JsonProperty(value = "photo_url")
    protected String photoUrl;

    @JsonProperty(value = "last_bid")
    private Long lastBid;

    public ForSaleItemResponseMainInformationDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getLastBid() {
        return lastBid;
    }

    public void setLastBid(Long lastBid) {
        this.lastBid = lastBid;
    }
}
