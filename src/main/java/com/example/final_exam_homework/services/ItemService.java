package com.example.final_exam_homework.services;

import com.example.final_exam_homework.dtos.ForSaleItemRequestDTO;
import com.example.final_exam_homework.dtos.ForSaleItemResponseDTO;
import com.example.final_exam_homework.dtos.ForSaleItemResponseMainInformationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    ForSaleItemResponseDTO createForSaleItemAndReturnResponse(ForSaleItemRequestDTO forSaleItemRequestDTO, String header);

    List<ForSaleItemResponseMainInformationDTO> getForSaleItemList(int page);

    Pageable createPageable(int page);
}
