package com.example.final_exam_homework.unit;

import com.example.final_exam_homework.dtos.ForSaleItemRequestDTO;
import com.example.final_exam_homework.dtos.ForSaleItemResponseDTO;
import com.example.final_exam_homework.exceptions.MissingNameException;
import com.example.final_exam_homework.models.User;
import com.example.final_exam_homework.repositories.ItemRepository;
import com.example.final_exam_homework.services.ItemService;
import com.example.final_exam_homework.services.ItemServiceImpl;
import com.example.final_exam_homework.services.UserService;
import com.example.final_exam_homework.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class ItemServiceTest {

    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private final ModelMapper modelMapper = new ModelMapper();
    private final UserService userService = Mockito.mock(UserService.class);
    private final JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    private final ItemService itemService = new ItemServiceImpl(itemRepository, userService, modelMapper, jwtUtil);

    @Test
    public void createForSaleItemResponse_WithValidInformation_ShouldReturnForSaleItemResponseDTO() {
        User user = new User();
        String headerTest = "$2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge";
        ForSaleItemRequestDTO forSaleItemRequestDTOTest = new ForSaleItemRequestDTO();
        forSaleItemRequestDTOTest.setName("BATMAN KNIGHTWATCH #1");
        forSaleItemRequestDTOTest.setDescription("After a massive breakout at Arkham Asylum, Batman and his team are on a mission to bring all the escapees back.");
        forSaleItemRequestDTOTest.setPhotoUrl("https://davescomicshop.com/wp-content/uploads/2022/07/0722DC091.jpg");
        forSaleItemRequestDTOTest.setStartingPrice(129);
        forSaleItemRequestDTOTest.setPurchasePrice(1299);
        Mockito.when(jwtUtil.extractUsername("$2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge")).thenReturn("Theodred Herstina");
        Mockito.when(userService.findUserByUsername(jwtUtil.extractUsername("$2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge"))).thenReturn(user);

        ForSaleItemResponseDTO actualForSaleItemResponseDTO = itemService.createForSaleItemAndReturnResponse(forSaleItemRequestDTOTest, headerTest);

        Assertions.assertEquals(forSaleItemRequestDTOTest.getName(), actualForSaleItemResponseDTO.getName());
        Assertions.assertEquals(forSaleItemRequestDTOTest.getDescription(), actualForSaleItemResponseDTO.getDescription());
        Assertions.assertEquals(forSaleItemRequestDTOTest.getPhotoUrl(), actualForSaleItemResponseDTO.getPhotoUrl());
        Assertions.assertEquals(forSaleItemRequestDTOTest.getStartingPrice(), actualForSaleItemResponseDTO.getStartingPrice());
        Assertions.assertEquals(forSaleItemRequestDTOTest.getPurchasePrice(), actualForSaleItemResponseDTO.getPurchasePrice());
    }


}
