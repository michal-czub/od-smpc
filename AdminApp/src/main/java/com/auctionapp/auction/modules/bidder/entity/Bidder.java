package com.auctionapp.auction.modules.bidder.entity;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Entity
@Getter
@Setter
public class Bidder
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) public int bidderId;
    public String firstName;
    public String surname;

    @GeneratedValue(strategy = GenerationType.AUTO)
    String privateKey = "";


    @GeneratedValue(strategy = GenerationType.AUTO)
    public boolean hasAlreadyBid = false;
    @GeneratedValue(strategy = GenerationType.AUTO)
    public boolean inAuction = false;
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String valueOfBid = "";

    @GeneratedValue(strategy = GenerationType.AUTO)
    int wonMillionaireProblemCount = 0;

}
