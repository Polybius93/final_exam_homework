package com.example.final_exam_homework.unit;

import com.example.final_exam_homework.dtos.BidOnItemRequestDTO;
import com.example.final_exam_homework.dtos.ForSaleItemRequestDTO;
import com.example.final_exam_homework.dtos.ForSaleItemResponseBuyersViewDTO;
import com.example.final_exam_homework.dtos.ForSaleItemResponseSellersViewDTO;
import com.example.final_exam_homework.exceptions.InsufficientGreenBayDollarsException;
import com.example.final_exam_homework.exceptions.ItemIsAlreadySoldException;
import com.example.final_exam_homework.exceptions.ItemNotFoundException;
import com.example.final_exam_homework.exceptions.TooLowBidException;
import com.example.final_exam_homework.models.Bid;
import com.example.final_exam_homework.models.Item;
import com.example.final_exam_homework.models.User;
import com.example.final_exam_homework.repositories.BidRepository;
import com.example.final_exam_homework.repositories.ItemRepository;
import com.example.final_exam_homework.repositories.UserRepository;
import com.example.final_exam_homework.services.*;
import com.example.final_exam_homework.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private final BidRepository bidRepository = Mockito.mock(BidRepository.class);
    private final BidService bidService = Mockito.mock(BidServiceImpl.class);
    private final ModelMapper modelMapper = new ModelMapper();
    private final UserService userService = Mockito.mock(UserService.class);
    private final JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    private final ItemService itemService = new ItemServiceImpl(userRepository, itemRepository, bidRepository, bidService, userService, modelMapper, jwtUtil);

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

        ForSaleItemResponseSellersViewDTO actualForSaleItemResponseSellersViewDTO = itemService.createForSaleItemAndReturnResponse(forSaleItemRequestDTOTest, headerTest);

        Assertions.assertEquals(forSaleItemRequestDTOTest.getName(), actualForSaleItemResponseSellersViewDTO.getName());
        Assertions.assertEquals(forSaleItemRequestDTOTest.getDescription(), actualForSaleItemResponseSellersViewDTO.getDescription());
        Assertions.assertEquals(forSaleItemRequestDTOTest.getPhotoUrl(), actualForSaleItemResponseSellersViewDTO.getPhotoUrl());
        Assertions.assertEquals(forSaleItemRequestDTOTest.getStartingPrice(), actualForSaleItemResponseSellersViewDTO.getStartingPrice());
        Assertions.assertEquals(forSaleItemRequestDTOTest.getPurchasePrice(), actualForSaleItemResponseSellersViewDTO.getPurchasePrice());
    }

    @Test
    public void createPageable_WithValidInformation_ShouldReturnPageable() {
        Pageable pageable = itemService.createPageable(0);

        Assertions.assertEquals(0, pageable.getPageNumber());
        Assertions.assertEquals(20, pageable.getPageSize());
    }

    @Test
    public void showForSaleItemResponseBuyersViewDTO_WithValidInformation_ShouldReturnForSaleItemResponseBuyersViewDTO() {
        User user = new User();
        user.setUsername("Zeno Petceran");
        Item item = new Item();
        item.setName("ACTION COMICS #1047");
        item.setSeller(user);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ForSaleItemResponseBuyersViewDTO forSaleItemResponseBuyersViewDTO = itemService.showForSaleItemResponseBuyersViewDTO(1L);

        Assertions.assertEquals(item.getName(), forSaleItemResponseBuyersViewDTO.getName());
    }

    @Test
    public void showForSaleItemResponseBuyersViewDTO_WithInvalidInformation_ShouldReturnForSaleItemResponseBuyersViewDTO() {
        String expectedMessage = "No item was found by this id!";
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.showForSaleItemResponseBuyersViewDTO(1L));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void bidForSaleItem_WithValidInput_ShouldReturnShouldBidAndReturnRequestedItem() {
        String header = "Bearer $2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge";
        User userTest1 = new User();
        userTest1.setUsername("Zeno Petceran");
        userTest1.setGreenBayDollars(2000L);

        User userTest2 = new User();
        userTest2.setUsername("Theodred Herstina");

        Item item = new Item();
        item.setName("ACTION COMICS #1047");
        item.setSeller(userTest2);
        item.setSold(false);
        item.setPurchasePrice(2199);

        Bid bidTest1 = new Bid();
        bidTest1.setValue(219L);

        BidOnItemRequestDTO bidOnItemRequestDTO = new BidOnItemRequestDTO();
        bidOnItemRequestDTO.setBid(319L);

        List<Bid> bidListTest1 = new ArrayList<>();
        Bid bidTest2 = new Bid();
        bidTest2.setValue(319L);
        bidTest2.setUser(userTest1);
        bidListTest1.add(bidTest2);

        Mockito.when(bidService.findLastBidByItemId(1L)).thenReturn(bidTest1);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(userService.findUserByUsername(jwtUtil.extractUsername("$2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge"))).thenReturn(userTest1);
        Mockito.when(bidRepository.findAllByItemOrderByIdDesc(item)).thenReturn(bidListTest1);

        ForSaleItemResponseBuyersViewDTO forSaleItemResponseBuyersViewDTO = itemService.bidForSaleItem(bidOnItemRequestDTO, header, 1L);

        Assertions.assertEquals(userTest1.getUsername(), forSaleItemResponseBuyersViewDTO.getBidWithUsernameDTOList().get(0).getUsername());
        Assertions.assertEquals(319L, forSaleItemResponseBuyersViewDTO.getBidWithUsernameDTOList().get(0).getValue());
    }

    @Test
    public void bidForSaleItem_WithValidInput_ShouldBuyAndReturnRequestedItem() {
        String header = "Bearer $2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge";
        User userTest1 = new User();
        userTest1.setUsername("Zeno Petceran");
        userTest1.setGreenBayDollars(2200L);
        userTest1.setItemToBuyList(new ArrayList<>());

        User userTest2 = new User();
        userTest2.setUsername("Theodred Herstina");

        Item item = new Item();
        item.setName("ACTION COMICS #1047");
        item.setSeller(userTest2);
        item.setBuyer(userTest1);
        item.setSold(false);
        item.setPurchasePrice(2199);

        Bid bidTest1 = new Bid();
        bidTest1.setValue(219L);

        BidOnItemRequestDTO bidOnItemRequestDTO = new BidOnItemRequestDTO();
        bidOnItemRequestDTO.setBid(2199L);

        List<Bid> bidListTest1 = new ArrayList<>();
        Bid bidTest2 = new Bid();
        bidTest2.setValue(2199L);
        bidTest2.setUser(userTest1);
        bidListTest1.add(bidTest2);

        Mockito.when(bidService.findLastBidByItemId(1L)).thenReturn(bidTest1);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(userService.findUserByUsername(jwtUtil.extractUsername("$2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge"))).thenReturn(userTest1);
        Mockito.when(bidRepository.findAllByItemOrderByIdDesc(item)).thenReturn(bidListTest1);

        ForSaleItemResponseBuyersViewDTO forSaleItemResponseBuyersViewDTO = itemService.bidForSaleItem(bidOnItemRequestDTO, header, 1L);

        Assertions.assertEquals("sold", forSaleItemResponseBuyersViewDTO.getAvailable());
        Assertions.assertEquals(1L, userTest1.getGreenBayDollars());
    }

    @Test
    public void bidForSaleItem_WithInvalidInput_ShouldReturnItemNotFoundException() {
        String expectedMessage = "No item was found by this id!";
        String header = "Bearer $2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge";
        BidOnItemRequestDTO bidOnItemRequestDTO = new BidOnItemRequestDTO();
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.bidForSaleItem(bidOnItemRequestDTO, header, 1L));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void bidForSaleItem_WithInvalidInput_ShouldReturnTooLowBidException() {
        String expectedMessage = "Your bid is lower than the last bid!";
        String header = "Bearer $2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge";
        User userTest1 = new User();
        userTest1.setUsername("Zeno Petceran");
        userTest1.setGreenBayDollars(2200L);
        userTest1.setItemToBuyList(new ArrayList<>());

        User userTest2 = new User();
        userTest2.setUsername("Theodred Herstina");

        Item item = new Item();
        item.setName("ACTION COMICS #1047");
        item.setSeller(userTest2);
        item.setBuyer(userTest1);
        item.setSold(false);
        item.setPurchasePrice(2199);

        Bid bidTest1 = new Bid();
        bidTest1.setValue(219L);

        BidOnItemRequestDTO bidOnItemRequestDTO = new BidOnItemRequestDTO();
        bidOnItemRequestDTO.setBid(119L);

        List<Bid> bidListTest1 = new ArrayList<>();
        Bid bidTest2 = new Bid();
        bidTest2.setValue(119L);
        bidTest2.setUser(userTest1);
        bidListTest1.add(bidTest2);

        Mockito.when(bidService.findLastBidByItemId(1L)).thenReturn(bidTest1);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(userService.findUserByUsername(jwtUtil.extractUsername("$2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge"))).thenReturn(userTest1);
        Mockito.when(bidRepository.findAllByItemOrderByIdDesc(item)).thenReturn(bidListTest1);


        Exception exception = Assertions.assertThrows(TooLowBidException.class, () -> itemService.bidForSaleItem(bidOnItemRequestDTO, header, 1L));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void bidForSaleItem_WithValidInput_ShouldReturnInsufficientGreenBayDollarsException() {
        String expectedMessage = "Not enough Green Bay Dollars";
        String header = "Bearer $2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge";
        User userTest1 = new User();
        userTest1.setUsername("Zeno Petceran");
        userTest1.setGreenBayDollars(119L);
        userTest1.setItemToBuyList(new ArrayList<>());

        Item item = new Item();
        item.setSold(false);
        item.setPurchasePrice(2199);

        Bid bidTest1 = new Bid();
        bidTest1.setValue(219L);

        BidOnItemRequestDTO bidOnItemRequestDTO = new BidOnItemRequestDTO();
        bidOnItemRequestDTO.setBid(319L);

        Mockito.when(bidService.findLastBidByItemId(1L)).thenReturn(bidTest1);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(userService.findUserByUsername(jwtUtil.extractUsername("$2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge"))).thenReturn(userTest1);

        Exception exception = Assertions.assertThrows(InsufficientGreenBayDollarsException.class, () -> itemService.bidForSaleItem(bidOnItemRequestDTO, header, 1L));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void bidForSaleItem_WithInvalidInput_ShouldReturnItemIsAlreadySoldException() {
        String expectedMessage = "Item is already sold!";
        String header = "Bearer $2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge";
        User userTest1 = new User();
        userTest1.setUsername("Zeno Petceran");
        userTest1.setGreenBayDollars(119L);
        userTest1.setItemToBuyList(new ArrayList<>());

        Item item = new Item();
        item.setSold(true);
        item.setPurchasePrice(2199);

        Bid bidTest1 = new Bid();
        bidTest1.setValue(219L);

        BidOnItemRequestDTO bidOnItemRequestDTO = new BidOnItemRequestDTO();
        bidOnItemRequestDTO.setBid(319L);

        Mockito.when(bidService.findLastBidByItemId(1L)).thenReturn(bidTest1);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(userService.findUserByUsername(jwtUtil.extractUsername("$2a$11$vYTXBdEm8oLIYD8wdTJKnORPexavwc7x6Y2wT8WMajOFM3rHpSKge"))).thenReturn(userTest1);

        Exception exception = Assertions.assertThrows(ItemIsAlreadySoldException.class, () -> itemService.bidForSaleItem(bidOnItemRequestDTO, header, 1L));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
}
