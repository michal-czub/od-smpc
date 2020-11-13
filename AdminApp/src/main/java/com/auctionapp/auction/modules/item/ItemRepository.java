package com.auctionapp.auction.modules.item;

import com.auctionapp.auction.modules.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>
{

}

