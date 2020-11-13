package com.auctionapp.auction.modules.auction.entity;

import com.auctionapp.auction.modules.bidder.entity.Bidder;
import com.auctionapp.auction.modules.item.entity.Item;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int auctionId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId")
    public Item item;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="bidderId")
//    public Bidder bidder;


    @Nullable
    @ElementCollection
    @Column(name="bidders_ids")
    public List<Integer> biddersIds;
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int auctionersNumber = 0;

    public Auction(Item item)
    {
        this.item = item;
    }

    public Auction() {

    }
}
