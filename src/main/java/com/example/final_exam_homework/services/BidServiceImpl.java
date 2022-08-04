package com.example.final_exam_homework.services;

import com.example.final_exam_homework.models.Bid;
import com.example.final_exam_homework.repositories.BidRepository;
import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;

    public BidServiceImpl(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @Override
    public Bid findLastBidByItemId(Long itemId) {
        Bid bid = bidRepository.findLastBid(itemId).get(0);
        return bid;
    }
}
