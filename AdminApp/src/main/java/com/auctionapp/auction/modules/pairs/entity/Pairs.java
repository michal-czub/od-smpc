package com.auctionapp.auction.modules.pairs.entity;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Pairs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) public int pairsId;


    @GeneratedValue(strategy = GenerationType.AUTO)
    String e="";


    @GeneratedValue(strategy = GenerationType.AUTO)
    String n="";

    public Pairs(String e, String n)
    {
        this.e = e;
        this.n = n;
    }
    public Pairs()
    {

    }
}
