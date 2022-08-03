package com.example.final_exam_homework.controllers;

import com.example.final_exam_homework.dtos.ForSaleItemRequestDTO;
import com.example.final_exam_homework.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/sell")
    public ResponseEntity<?> createForSaleItem(
            @RequestHeader(value = "Authorization") String header,
            @RequestBody ForSaleItemRequestDTO forSaleItemRequestDTO) {
        return ResponseEntity.ok(itemService.createForSaleItemAndReturnResponse(forSaleItemRequestDTO, header));
    }
}
