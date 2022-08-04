package com.example.final_exam_homework.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "id", "last_bid", "starting_price" })
public class ForSaleItemResponseBuyersViewSoldDTO extends ForSaleItemResponseBuyersViewDTO {

    private String available = "sold";

    @JsonProperty(value = "buyer")
    private String buyerUsername;

    public ForSaleItemResponseBuyersViewSoldDTO() {
    }

    public String getAvailable() {
        return available;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }
}
