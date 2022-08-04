package com.example.final_exam_homework.services;

import com.example.final_exam_homework.models.Bid;

public interface BidService {

    Bid findLastBidByItemId(Long itemId);
}
