package com.auctionapp.auction.modules.bidder;

import com.auctionapp.auction.modules.bidder.entity.Bidder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("/bidder")
public class BidderController {

    private final BidderService bidderService;
    @PostMapping("/add")
    Bidder addBidder(@RequestBody Bidder newBidder)
    {
        return bidderService.addBidder(newBidder);

    }
    @GetMapping("/get-all")
    public Iterable<Bidder> getBidders()
    {
        return bidderService.getBidders();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBidder(@PathVariable int id)
    {
        return bidderService.deleteBidder(id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBidder(@PathVariable int id)
    {
        return bidderService.getBidder(id);
    }
}
