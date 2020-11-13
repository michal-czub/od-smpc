package com.auctionapp.auction.modules.bidder;

import com.auctionapp.auction.modules.PrimeList;
import com.auctionapp.auction.modules.bidder.entity.Bidder;
import com.auctionapp.auction.modules.pairs.PairsRepository;
import com.auctionapp.auction.modules.pairs.entity.Pairs;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BidderService {
    public final PrimeList primeList = new PrimeList();
    private final List<Integer> list = primeList.PrimeList();
    private final BidderRepository bidderRepository;
    private final PairsRepository pairsRepository;

    public Bidder addBidder(Bidder bidder) {
        int p = PrimeList.getRandom(list);
        int q = PrimeList.getRandom(list);
        int phi = (p - 1) * (q - 1);
        int n = p * q;
        int e = primeList.E_gen(phi, p, q);
        Pairs pairs = new Pairs(e, n);
        pairsRepository.save(pairs);
        bidder.setD(primeList.D_gen(phi,p,q,e));
        return bidderRepository.save(bidder);
    }

    public Iterable<Bidder> getBidders() {
        return bidderRepository.findAll();
    }

    public ResponseEntity<?> getBidder(int id) {
        try {
            Bidder bidder = bidderRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(bidder, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Bidder with ID: " + id + " doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    ResponseEntity<String> deleteBidder(int id) {

        try {
            Bidder bidder = bidderRepository.findById(id).orElseThrow();
            if (bidder.isInAuction()) {
                return new ResponseEntity<>("This bidder participates in an auction thus he cannot be deleted", HttpStatus.OK);
            }

            bidderRepository.deleteById(id);
            return new ResponseEntity<>("Bidder with ID: " + id + " was deleted", HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Bidder with ID: " + id + " doesn't exist", HttpStatus.OK);
        }
    }
}
