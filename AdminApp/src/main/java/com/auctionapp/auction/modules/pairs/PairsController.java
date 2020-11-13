package com.auctionapp.auction.modules.pairs;
import com.auctionapp.auction.modules.pairs.entity.Pairs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("/pairs")
public class PairsController {
    private final PairsService pairsService;
    @GetMapping("/get-all")
    public Iterable<Pairs> getPairs()
    {
        return pairsService.getPairs();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBidder(@PathVariable int id)
    {
        return pairsService.getPair(id);
    }
}
