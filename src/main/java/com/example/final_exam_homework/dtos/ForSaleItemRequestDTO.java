package com.example.final_exam_homework.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForSaleItemRequestDTO {

    private String name;
    private String description;

    @JsonProperty(value = "photo_url")
    private String photoUrl;

    @JsonProperty(value = "starting_price")
    private int startingPrice;

    @JsonProperty(value = "purchase_price")
    private int purchasePrice;

    public ForSaleItemRequestDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}
