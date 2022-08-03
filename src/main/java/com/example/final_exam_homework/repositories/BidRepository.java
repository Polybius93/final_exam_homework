package com.example.final_exam_homework.repositories;

import com.example.final_exam_homework.models.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query(value = "SELECT * FROM BIDS WHERE item_id = :item_id ORDER BY id DESC LIMIT 1", nativeQuery = true)
    List<Bid> findLastBid(@Param("item_id") Long itemID);
}
