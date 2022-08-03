package com.example.final_exam_homework.repositories;

import com.example.final_exam_homework.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

    Page<Item> findAllBySold(boolean sold, Pageable pageable);
}
