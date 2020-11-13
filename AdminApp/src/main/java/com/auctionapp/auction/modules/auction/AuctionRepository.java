package com.auctionapp.auction.modules.auction;

import com.auctionapp.auction.modules.auction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
}
