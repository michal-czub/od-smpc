package com.auctionapp.auction.modules;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class Keys {
    private final static SecureRandom random = new SecureRandom();
    private final static BigInteger one = new BigInteger("1");
    public BigInteger privateKey;
    public BigInteger publicKey;
    public BigInteger n;
    BigInteger phi;
    BigInteger p;
    BigInteger q;
    public Keys()
    {
        this.p = BigInteger.probablePrime(10,random);
        this.q = BigInteger.probablePrime(10,random);
        this.phi = (p.subtract(one)).multiply(q.subtract(one));
        this.n = p.multiply(q);
        this.publicKey = new BigInteger("467");
        this.privateKey = publicKey.modInverse(phi); //d = (publicKey^-1) * mod(phi)

    }

    public String getE()
    {
        return publicKey.toString();
    }
    public String getN()
    {
        return n.toString();
    }
    public String getD()
    {
        return privateKey.toString();
    }

}
