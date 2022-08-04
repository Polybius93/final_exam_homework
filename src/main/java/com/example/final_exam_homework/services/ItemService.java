package com.example.final_exam_homework.services;

import com.example.final_exam_homework.dtos.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    ForSaleItemResponseSellersViewDTO createForSaleItemAndReturnResponse(ForSaleItemRequestDTO forSaleItemRequestDTO, String header);

    List<ForSaleItemResponseMainInformationDTO> getForSaleItemList(int page);

    Pageable createPageable(int page);

    ForSaleItemResponseBuyersViewDTO showForSaleItemResponseBuyersViewDTO(Long itemId);

    ForSaleItemResponseBuyersViewDTO bidForSaleItem(BidOnItemRequestDTO bidOnItemRequestDTO, String header, Long itemId);
}
