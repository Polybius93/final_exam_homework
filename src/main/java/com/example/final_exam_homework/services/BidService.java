package com.example.final_exam_homework.services;

import com.example.final_exam_homework.models.Bid;
import com.example.final_exam_homework.models.Item;
import com.example.final_exam_homework.repositories.BidRepository;
import com.example.final_exam_homework.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;

    public BidService(BidRepository bidRepository, ItemRepository itemRepository) {
        this.bidRepository = bidRepository;
        this.itemRepository = itemRepository;
    }

    public Bid findLastBidByItemId(Long itemId) {
        Bid bid = new Bid();
        try {
            bid = bidRepository.findLastBid(itemId).get(0);
        } catch (NullPointerException nullPointerException) {
            Item item = itemRepository.findById(itemId).get();
            bid.setUser(item.getSeller());
            bid.setItem(item);
            item.getBidList().add(bid);
            itemRepository.save(item);
        }
        return bid;
    }
}
