package com.auctionapp.auction.modules.auction;

import com.auctionapp.auction.modules.auction.entity.Auction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {
    private final AuctionService auctionService;

    @PostMapping("/add")
    ResponseEntity<?> addAuction(@RequestBody Map<String, String> auction) {
        return auctionService.addAuction(auction);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getAuction(@PathVariable int id) {
        return auctionService.getAuction(id);
    }

    @GetMapping("/get-all")
    Iterable<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @DeleteMapping("/delete/{id}")

    ResponseEntity<String> deleteAuction(@PathVariable int id) {
        return auctionService.deleteAuction(id);
    }

    @PostMapping("/add-bidder/{auctionId}/{bidderId}")
    ResponseEntity<?> addBidder(@PathVariable int auctionId, @PathVariable int bidderId) {
        return auctionService.addBidder(auctionId, bidderId);
    }

    @GetMapping("/get-bidders/{auctionId}")
    ResponseEntity<?> getBiddersFromChosenAuction(@PathVariable int auctionId) {
        return auctionService.getAllBiddersFromChosenAuction(auctionId);
    }

    @DeleteMapping("/delete-bidder/{auctionId}/{bidderId}")
    ResponseEntity<?> deleteBidderFromChosenAuction(@PathVariable int auctionId, @PathVariable int bidderId)
    {
        return auctionService.removeBidderFromChosenAuction(auctionId,bidderId);
    }
    @PostMapping("/start/{auctionId}")
    ResponseEntity<?> startAuction(@PathVariable int auctionId)
    {
        return auctionService.auctionStart(auctionId);
    }
}
