package com.example.final_exam_homework.controllers;

import com.example.final_exam_homework.dtos.BidOnItemRequestDTO;
import com.example.final_exam_homework.dtos.ForSaleItemRequestDTO;
import com.example.final_exam_homework.services.ItemService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public ResponseEntity<?> listForSaleItems(@RequestParam int page) {
        return ResponseEntity.ok(itemService.getForSaleItemList(page));
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> showItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.showForSaleItemResponseBuyersViewDTO(itemId));
    }

    @PostMapping("/item/{itemId}")
    public ResponseEntity<?> bidOnItem(@RequestHeader(value = "Authorization") String header, @PathVariable Long itemId, @RequestBody BidOnItemRequestDTO bidOnItemRequestDTO) {
        return ResponseEntity.ok(itemService.bidForSaleItem(bidOnItemRequestDTO, header, itemId));
    }
}
