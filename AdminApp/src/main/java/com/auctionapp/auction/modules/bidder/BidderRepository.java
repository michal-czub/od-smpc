package com.auctionapp.auction.modules.bidder;

import com.auctionapp.auction.modules.bidder.entity.Bidder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidderRepository extends JpaRepository<Bidder, Integer> {
}
