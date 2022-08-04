package com.example.final_exam_homework.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForSaleItemResponseSellersViewDTO extends ForSaleItemResponseMainInformationDTO {

    private Long id;
    protected String description;

    @JsonProperty(value = "starting_price")
    private int startingPrice;

    @JsonProperty(value = "purchase_price")
    protected int purchasePrice;

    public ForSaleItemResponseSellersViewDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
