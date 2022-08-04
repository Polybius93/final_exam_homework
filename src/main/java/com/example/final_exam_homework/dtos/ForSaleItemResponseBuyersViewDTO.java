package com.example.final_exam_homework.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties({ "id", "last_bid", "starting_price" })
public class ForSaleItemResponseBuyersViewDTO extends ForSaleItemResponseSellersViewDTO {

    private String available = "for sale";

    @JsonProperty(value = "bids")
    protected List<BidWithUsernameDTO> bidWithUsernameDTOList;

    @JsonProperty(value = "seller")
    protected String sellerUsername;

    public ForSaleItemResponseBuyersViewDTO() {}

    public String getAvailable() {
        return available;
    }

    public List<BidWithUsernameDTO> getBidWithUsernameDTOList() {
        return bidWithUsernameDTOList;
    }

    public void setBidWithUsernameDTOList(List<BidWithUsernameDTO> bidWithUsernameDTOList) {
        this.bidWithUsernameDTOList = bidWithUsernameDTOList;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }
}
