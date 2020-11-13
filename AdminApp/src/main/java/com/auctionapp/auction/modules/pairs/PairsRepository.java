package com.auctionapp.auction.modules.pairs;

import com.auctionapp.auction.modules.pairs.entity.Pairs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PairsRepository extends JpaRepository<Pairs,Integer> {
}
