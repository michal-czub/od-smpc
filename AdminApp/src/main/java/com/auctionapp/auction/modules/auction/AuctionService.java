package com.auctionapp.auction.modules.auction;

import com.auctionapp.auction.modules.auction.entity.Auction;
import com.auctionapp.auction.modules.bidder.BidderRepository;
import com.auctionapp.auction.modules.bidder.BidderService;
import com.auctionapp.auction.modules.bidder.entity.Bidder;
import com.auctionapp.auction.modules.item.ItemRepository;
import com.auctionapp.auction.modules.item.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor

public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final BidderRepository bidderRepository;
    private final ItemRepository itemRepository;
    private final BidderService bidderService;

    ResponseEntity<?> addAuction(Map<String, String> auction) {
        int idItem = Integer.parseInt(auction.get("item"));
        Item item = itemRepository.findById(idItem).orElseThrow();
        if (item.isBeingAuctioned()) {
            return new ResponseEntity<>("This item cannot be added to auction", HttpStatus.OK);
        } else {
            item.setBeingAuctioned(true);
            auctionRepository.save(new Auction(item));
            return new ResponseEntity<>("Auction with: " + item.getName() + " succesfully created", HttpStatus.OK);
        }
    }

    ResponseEntity<?> getAuction(int id) {
        try {
            Auction auction = auctionRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(auction, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("There is no such auction with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    Iterable<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    ResponseEntity<String> deleteAuction(int id) {
        try {
            Auction auction = auctionRepository.findById(id).orElseThrow();
            auction.item.setBeingAuctioned(false);
            List<Integer> ids = auction.biddersIds;
            if (ids.size() > 0) {
                for (int i : ids) {
                    Bidder bidder = bidderRepository.findById(i).orElseThrow();
                    bidder.setInAuction(false);
                    bidderRepository.save(bidder);
                }
            }
            auctionRepository.deleteById(id);

            return new ResponseEntity<>("Auction with ID: " + id + " was deleted", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Auction with ID: " + id + " doesn't exist", HttpStatus.OK);
        }
    }

    ResponseEntity<?> addBidder(int auctionId, int bidderId) {
        try {
            Auction auction = auctionRepository.findById(auctionId).orElseThrow();
            Bidder bidder = bidderRepository.findById(bidderId).orElseThrow();
            if (bidder.isInAuction()) {
                return new ResponseEntity<String>("Selected bidder is already in auction", HttpStatus.OK);
            } else if (auction.getAuctionersNumber() >= 5) {
                return new ResponseEntity<String>("Auctionis overloaded", HttpStatus.OK);
            } else {
                auction.auctionersNumber++;
                auction.biddersIds.add(bidderId);
                bidder.setInAuction(true);
                auctionRepository.save(auction);
                bidderRepository.save(bidder);
                return new ResponseEntity<String>("successfully added bidder with name: " + bidder.getFirstName() + " to auction with ID: " + auction.getAuctionId(), HttpStatus.OK);
            }

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Bidder or auction doesn't exist!", HttpStatus.OK);
        }
    }

    ResponseEntity<?> getAllBiddersFromChosenAuction(int auctionId) {
        try {
            Auction auction = auctionRepository.findById(auctionId).orElseThrow();
            List<Integer> ids = auction.getBiddersIds();
            List<Bidder> list = new ArrayList<>();
            if (auction.biddersIds.size() == 0) {
                return new ResponseEntity<>("Currently this auction has no bidders", HttpStatus.OK);
            }
            for (int i : ids) {
                Bidder bidder = bidderRepository.findById(i).orElseThrow();
                list.add(bidder);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Auction doesn't exist!", HttpStatus.OK);
        }
    }

    ResponseEntity<?> removeBidderFromChosenAuction(int auctionId, int bidderId)
    {
        try{
            Auction auction = auctionRepository.findById(auctionId).orElseThrow();
            List<Integer> list = auction.getBiddersIds();
            if (auction.biddersIds.size() == 0) {
                return new ResponseEntity<>("Currently this auction has no bidders", HttpStatus.OK);
            }
            if(list.contains(bidderId))
            {
                Bidder bidder = bidderRepository.findById(bidderId).orElseThrow();
                bidder.setInAuction(false);
                auction.setAuctionersNumber(auction.getAuctionersNumber()-1);
                list.remove(Integer.valueOf(bidderId));
                auctionRepository.save(auction);
                bidderRepository.save(bidder);

                return new ResponseEntity<>("Succesfully deleted bidder with ID " + bidderId + " from this auction",HttpStatus.OK);
            }
            else
        return new ResponseEntity<>("This bidder isn't in this auction",HttpStatus.OK);
        }catch(NoSuchElementException e)
        {
            return new ResponseEntity<>("Auction doesn't exist", HttpStatus.OK);
        }

    }
}
