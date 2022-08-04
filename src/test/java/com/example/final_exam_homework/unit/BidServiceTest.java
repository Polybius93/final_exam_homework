package com.example.final_exam_homework.unit;

import com.example.final_exam_homework.models.Bid;
import com.example.final_exam_homework.repositories.BidRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class BidServiceTest {

    private final BidRepository bidRepository = Mockito.mock(BidRepository.class);

    @Test
    public void findLastBidByItemId_WithValidInformation_ShouldReturnLastBid() {
        Bid bid = new Bid();
        List<Bid> bidListExpected = new ArrayList<>();
        bidListExpected.add(bid);
        Mockito.when(bidRepository.findLastBid(1L)).thenReturn(bidListExpected);

        List<Bid> bidListActual = bidRepository.findLastBid(1L);

        Assertions.assertEquals(bidListExpected.size(), bidListActual.size());
        Assertions.assertEquals(bidListExpected.get(0), bidListActual.get(0));
    }
}
