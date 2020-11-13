package com.auctionapp.auction.modules.item.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Entity
@Getter
@Setter
public class Item
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) public int itemId;

    public String name;
    public int year;
    public long value;
    @GeneratedValue(strategy = GenerationType.AUTO)
    public boolean beingAuctioned = false;
}

