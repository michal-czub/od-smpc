package com.auctionapp.auction.modules.bidder;

import com.auctionapp.auction.modules.AES;
import com.auctionapp.auction.modules.Keys;

import com.auctionapp.auction.modules.bidder.entity.Bidder;
import com.auctionapp.auction.modules.pairs.PairsRepository;
import com.auctionapp.auction.modules.pairs.entity.Pairs;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BidderService {

    private final BidderRepository bidderRepository;
    private final PairsRepository pairsRepository;
    AES aes = new AES();
    public Bidder addBidder(Bidder bidder) {

        Keys keys = new Keys();
        Pairs pairs = new Pairs(keys.getE(), keys.getN());
        pairsRepository.save(pairs);
        String encrypted = AES.encrypt(keys.getD(),bidder.getSurname());
        bidder.setPrivateKey(encrypted);
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
            pairsRepository.deleteById(id);
            return new ResponseEntity<>("Bidder with ID: " + id + " was deleted", HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Bidder with ID: " + id + " doesn't exist", HttpStatus.OK);
        }
    }
}
