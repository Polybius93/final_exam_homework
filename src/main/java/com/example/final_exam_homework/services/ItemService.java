package com.example.final_exam_homework.services;

import com.example.final_exam_homework.dtos.ForSaleItemRequestDTO;
import com.example.final_exam_homework.dtos.ForSaleItemResponseDTO;

public interface ItemService {
    ForSaleItemResponseDTO createForSaleItemAndReturnResponse(ForSaleItemRequestDTO forSaleItemRequestDTO, String header);
}
