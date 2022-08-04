package com.example.final_exam_homework.services;

import com.example.final_exam_homework.dtos.*;
import com.example.final_exam_homework.exceptions.*;
import com.example.final_exam_homework.models.Bid;
import com.example.final_exam_homework.models.Item;
import com.example.final_exam_homework.models.User;
import com.example.final_exam_homework.repositories.BidRepository;
import com.example.final_exam_homework.repositories.ItemRepository;
import com.example.final_exam_homework.repositories.UserRepository;
import com.example.final_exam_homework.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BidRepository bidRepository;
    private final BidService bidService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    public ItemServiceImpl(UserRepository userRepository, ItemRepository itemRepository, BidRepository bidRepository, BidService bidService, UserService userService, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bidRepository = bidRepository;
        this.bidService = bidService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    public Item findItemById(Long itemId) {
        if (itemRepository.findById(itemId).isPresent()) {
            return itemRepository.findById(itemId).get();
        } else {
            throw new ItemNotFoundException("No item found by this id!");
        }
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
        } else if (forSaleItemRequestDTO.getStartingPrice() < 0) {
            throw new InvalidPriceException("Given price is invalid!");
        } else if (forSaleItemRequestDTO.getPurchasePrice() < 0) {
            throw new InvalidPriceException("Given price is invalid!");
        }
    }

    private Item createForSaleItem(ForSaleItemRequestDTO forSaleItemRequestDTO, String jwt) {
        Item item = new Item();
        Bid bid = new Bid();
        item.setSeller(userService.findUserByUsername(jwtUtil.extractUsername(jwt)));
        modelMapper.map(forSaleItemRequestDTO, item);
        bid.setValue((long) item.getStartingPrice());
        bid.setUser(item.getSeller());
        item.getBidList().add(bid);
        itemRepository.save(item);
        return item;
    }

    private ForSaleItemResponseSellersViewDTO createForSaleItemResponse(Item item) {
        ForSaleItemResponseSellersViewDTO forSaleItemResponseSellersViewDTO = new ForSaleItemResponseSellersViewDTO();
        modelMapper.map(item, forSaleItemResponseSellersViewDTO);
        return forSaleItemResponseSellersViewDTO;
    }

    @Override
    public ForSaleItemResponseSellersViewDTO createForSaleItemAndReturnResponse(ForSaleItemRequestDTO forSaleItemRequestDTO, String header) {
        String jwt = header.substring(7);
        validateForSaleItemRequest(forSaleItemRequestDTO);
        return createForSaleItemResponse(createForSaleItem(forSaleItemRequestDTO, jwt));
    }

    @Override
    public Pageable createPageable(int page) {
        if (page >= 0) {
            return PageRequest.of(page, 20, Sort.by("id"));
        } else {
            throw new InvalidPageException("Page must be a positive number!");
        }
    }

    private ForSaleItemResponseMainInformationDTO createForSaleItemResponseMainInformationDTO(Item item) {
        ForSaleItemResponseMainInformationDTO forSaleItemResponseMainInformationDTO = new ForSaleItemResponseMainInformationDTO();
        Bid lastBid = bidService.findLastBidByItemId(item.getId());
        modelMapper.map(item, forSaleItemResponseMainInformationDTO);
        forSaleItemResponseMainInformationDTO.setLastBid(lastBid.getValue());
        return forSaleItemResponseMainInformationDTO;
    }

    private List<ForSaleItemResponseMainInformationDTO> createForSaleItemResponseMainInformationDTOList(List<Item> itemList) {
        List<ForSaleItemResponseMainInformationDTO> forSaleItemResponseMainInformationDTOList = new ArrayList<>();
        for (Item item : itemList) {
            forSaleItemResponseMainInformationDTOList.add(createForSaleItemResponseMainInformationDTO(item));
        }
        return forSaleItemResponseMainInformationDTOList;
    }

    public List<ForSaleItemResponseMainInformationDTO> getForSaleItemList(int page) {
        return createForSaleItemResponseMainInformationDTOList(itemRepository.findAllBySold(
                false, createPageable(page)).getContent());
    }

    private BidWithUsernameDTO createBidWithUsernameDTO(Bid bid) {
        BidWithUsernameDTO bidWithUsernameDTO = new BidWithUsernameDTO();
        bidWithUsernameDTO.setUsername(bid.getUser().getUsername());
        bidWithUsernameDTO.setValue(bid.getValue());
        return bidWithUsernameDTO;
    }

    private List<BidWithUsernameDTO> createBidWithUsernameDTOList(List<Bid> bidList) {
        List<BidWithUsernameDTO> bidWithUsernameDTOList = new ArrayList<>();
        for (Bid bid : bidList) {
            bidWithUsernameDTOList.add(createBidWithUsernameDTO(bid));
        }
        return bidWithUsernameDTOList;
    }

    private void validateShowForSaleItemRequest(Long itemId) {
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new ItemNotFoundException("No item was found by this id!");
        }
    }

    private ForSaleItemResponseBuyersViewDTO createForSaleItemResponseBuyersViewDTO(Item item) {
        ForSaleItemResponseBuyersViewDTO forSaleItemResponseBuyersViewDTO = new ForSaleItemResponseBuyersViewDTO();
        List<Bid> bidList = bidRepository.findAllByItemOrderByIdDesc(item);
        modelMapper.map(item, forSaleItemResponseBuyersViewDTO);
        forSaleItemResponseBuyersViewDTO.setBidWithUsernameDTOList(createBidWithUsernameDTOList(bidList));
        forSaleItemResponseBuyersViewDTO.setSellerUsername(item.getSeller().getUsername());
        return forSaleItemResponseBuyersViewDTO;
    }

    private ForSaleItemResponseBuyersViewDTO createForSaleItemResponseBuyersViewSoldDTO(Item item) {
        ForSaleItemResponseBuyersViewSoldDTO forSaleItemResponseBuyersViewSoldDTO = new ForSaleItemResponseBuyersViewSoldDTO();
        List<Bid> bidList = bidRepository.findAllByItemOrderByIdDesc(item);
        modelMapper.map(item, forSaleItemResponseBuyersViewSoldDTO);
        forSaleItemResponseBuyersViewSoldDTO.setBidWithUsernameDTOList(createBidWithUsernameDTOList(bidList));
        forSaleItemResponseBuyersViewSoldDTO.setSellerUsername(item.getSeller().getUsername());
        forSaleItemResponseBuyersViewSoldDTO.setBuyerUsername(item.getBuyer().getUsername());
        return forSaleItemResponseBuyersViewSoldDTO;
    }

    public ForSaleItemResponseBuyersViewDTO showForSaleItemResponseBuyersViewDTO(Long itemId) {
        validateShowForSaleItemRequest(itemId);
        Item item = findItemById(itemId);
        if (!item.isSold()) {
            return createForSaleItemResponseBuyersViewDTO(item);
        } else {
            return createForSaleItemResponseBuyersViewSoldDTO(item);
        }
    }

    private void placeBid(User user, Item item, Long bidValue) {
        Bid bid = new Bid();
        bid.setUser(user);
        bid.setItem(item);
        bid.setValue(bidValue);
        item.getBidList().add(bid);
        itemRepository.save(item);
    }

    private void buyItem(User user, Item item) {
        item.setSold(true);
        item.setBuyer(user);
        user.setGreenBayDollars(user.getGreenBayDollars() - item.getPurchasePrice());
        user.getItemToBuyList().add(item);
        userRepository.save(user);
    }

    public ForSaleItemResponseBuyersViewDTO bidForSaleItem(BidOnItemRequestDTO bidOnItemRequestDTO, String header, Long itemId) {
        validateShowForSaleItemRequest(itemId);
        String jwt = header.substring(7);
        Long bid = bidOnItemRequestDTO.getBid();
        Item item = findItemById(itemId);
        Long lastBid = bidService.findLastBidByItemId(itemId).getValue();
        User user = userService.findUserByUsername(jwtUtil.extractUsername(jwt));
        if (item.isSold()) {
            throw new ItemIsAlreadySoldException("Item is already sold!");
        } else if (bid > user.getGreenBayDollars()) {
            throw new InsufficientGreenBayDollarsException("Not enough Green Bay Dollars");
        } else if (bid <= lastBid) {
            throw new TooLowBidException("Your bid is lower than the last bid!");
        } else if (bid < item.getPurchasePrice()) {
            placeBid(user, item, bid);
            return createForSaleItemResponseBuyersViewDTO(item);
        } else {
            buyItem(user, item);
            return createForSaleItemResponseBuyersViewSoldDTO(item);
        }
    }
}