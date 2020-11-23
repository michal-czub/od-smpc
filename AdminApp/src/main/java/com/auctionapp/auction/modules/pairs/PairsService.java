package com.auctionapp.auction.modules.pairs;

import com.auctionapp.auction.modules.bidder.entity.Bidder;

import com.auctionapp.auction.modules.pairs.entity.Pairs;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
@RequiredArgsConstructor
@Service
public class PairsService {



    private final PairsRepository pairsRepository;
    public Iterable<Pairs> getPairs()
    {
        return pairsRepository.findAll();
    }
    public ResponseEntity<?> getPair(int id)
    {
        try{
            Pairs pairs = pairsRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(pairs, HttpStatus.OK);
        }
        catch(NoSuchElementException e)
        {
            return new ResponseEntity<>("Pairs with ID: " + id + " don't exist",HttpStatus.NOT_FOUND);
        }
    }
}
