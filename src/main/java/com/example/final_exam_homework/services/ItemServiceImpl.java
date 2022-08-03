package com.example.final_exam_homework.services;

import com.example.final_exam_homework.dtos.ForSaleItemRequestDTO;
import com.example.final_exam_homework.dtos.ForSaleItemResponseDTO;
import com.example.final_exam_homework.exceptions.*;
import com.example.final_exam_homework.models.Item;
import com.example.final_exam_homework.repositories.ItemRepository;
import com.example.final_exam_homework.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    private void validateForSaleItemRequest(ForSaleItemRequestDTO forSaleItemRequestDTO) {
        if (forSaleItemRequestDTO.getName() == null || forSaleItemRequestDTO.getName().isBlank()) {
            throw new MissingNameException("Name is missing!");
        } else if (forSaleItemRequestDTO.getDescription() == null || forSaleItemRequestDTO.getDescription().isBlank()) {
            throw new MissingDescriptionException("Description is missing!");
        } else if (forSaleItemRequestDTO.getPhotoUrl() == null || forSaleItemRequestDTO.getPhotoUrl().isBlank()) {
            throw new MissingPhotoUrlException("Photo Url is missing!");
        } else if (!forSaleItemRequestDTO.getPhotoUrl().startsWith("https://") || !forSaleItemRequestDTO.getPhotoUrl().endsWith(".jpg")) {
            throw new InvalidPhotoUrlException("Photo Url is invalid!");
        } else if (forSaleItemRequestDTO.getStartingPrice() < 0 || forSaleItemRequestDTO.getStartingPrice() != (int) forSaleItemRequestDTO.getStartingPrice()) {
            throw new InvalidPriceException("Given price is invalid!");
        } else if (forSaleItemRequestDTO.getPurchasePrice() < 0 || forSaleItemRequestDTO.getPurchasePrice() != (int) forSaleItemRequestDTO.getPurchasePrice()) {
            throw new InvalidPriceException("Given price is invalid!");
        }
    }

    private Item createForSaleItem(ForSaleItemRequestDTO forSaleItemRequestDTO, String jwt) {
        Item item = new Item();
        item.setUser(userService.findUserByUsername(jwtUtil.extractUsername(jwt)));
        modelMapper.map(forSaleItemRequestDTO, item);
        itemRepository.save(item);
        return item;
    }

    private ForSaleItemResponseDTO createForSaleItemResponse(Item item) {
        ForSaleItemResponseDTO forSaleItemResponseDTO = new ForSaleItemResponseDTO();
        modelMapper.map(item, forSaleItemResponseDTO);
        return forSaleItemResponseDTO;
    }

    @Override
    public ForSaleItemResponseDTO createForSaleItemAndReturnResponse(ForSaleItemRequestDTO forSaleItemRequestDTO, String header) {
        String jwt = header.substring(7);
        validateForSaleItemRequest(forSaleItemRequestDTO);
        return createForSaleItemResponse(createForSaleItem(forSaleItemRequestDTO, jwt));
    }
}
