package com.auctionapp.auction.modules.auction;

import com.auctionapp.auction.modules.AES;
import com.auctionapp.auction.modules.auction.entity.Auction;
import com.auctionapp.auction.modules.bidder.BidderRepository;
import com.auctionapp.auction.modules.bidder.BidderService;
import com.auctionapp.auction.modules.bidder.entity.Bidder;
import com.auctionapp.auction.modules.item.ItemRepository;
import com.auctionapp.auction.modules.item.entity.Item;
import com.auctionapp.auction.modules.pairs.PairsRepository;
import com.auctionapp.auction.modules.pairs.entity.Pairs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
@RequiredArgsConstructor

public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final BidderRepository bidderRepository;
    private final ItemRepository itemRepository;
    private final PairsRepository pairsRepository;
    private final BidderService bidderService;
    static AES aes = new AES();
    ResponseEntity<?> addAuction(Map<String, String> auction) {
        int idItem = Integer.parseInt(auction.get("item"));
        Item item = itemRepository.findById(idItem).orElseThrow();
        if (item.isBeingAuctioned()) {
            return new ResponseEntity<>("This item cannot be added to auction", HttpStatus.OK);
        } else {
            item.setBeingAuctioned(true);
            auctionRepository.save(new Auction(item));
            return new ResponseEntity<>("Auction with: " + item.getName() + " succesfully created", HttpStatus.OK);
        }
    }

    ResponseEntity<?> getAuction(int id) {
        try {
            Auction auction = auctionRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(auction, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("There is no such auction with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    Iterable<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    ResponseEntity<String> deleteAuction(int id) {
        try {
            Auction auction = auctionRepository.findById(id).orElseThrow();
            auction.item.setBeingAuctioned(false);
            List<Integer> ids = auction.biddersIds;
            if (ids.size() > 0) {
                for (int i : ids) {
                    Bidder bidder = bidderRepository.findById(i).orElseThrow();
                    bidder.setInAuction(false);
                    bidderRepository.save(bidder);
                }
            }
            auctionRepository.deleteById(id);

            return new ResponseEntity<>("Auction with ID: " + id + " was deleted", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Auction with ID: " + id + " doesn't exist", HttpStatus.OK);
        }
    }

    ResponseEntity<?> addBidder(int auctionId, int bidderId) {
        try {
            Auction auction = auctionRepository.findById(auctionId).orElseThrow();
            Bidder bidder = bidderRepository.findById(bidderId).orElseThrow();
            if (bidder.isInAuction()) {
                return new ResponseEntity<>("Selected bidder is already in auction", HttpStatus.OK);
            } else if (auction.getAuctionersNumber() >= 5) {
                return new ResponseEntity<>("Auctionis overloaded", HttpStatus.OK);
            } else {
                auction.auctionersNumber++;
                auction.biddersIds.add(bidderId);
                bidder.setInAuction(true);
                auctionRepository.save(auction);
                bidderRepository.save(bidder);
                return new ResponseEntity<String>("successfully added bidder with name: " + bidder.getFirstName() + " to auction with ID: " + auction.getAuctionId(), HttpStatus.OK);
            }

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Bidder or auction doesn't exist!", HttpStatus.OK);
        }
    }

    ResponseEntity<?> getAllBiddersFromChosenAuction(int auctionId) {
        try {
            Auction auction = auctionRepository.findById(auctionId).orElseThrow();
            List<Integer> ids = auction.getBiddersIds();
            List<Bidder> list = new ArrayList<>();
            if (auction.biddersIds.size() == 0) {
                return new ResponseEntity<>("Currently this auction has no bidders", HttpStatus.OK);
            }
            for (int i : ids) {
                Bidder bidder = bidderRepository.findById(i).orElseThrow();
                bidder.setValueOfBid(AES.decrypt(bidder.getValueOfBid(),bidder.getSurname()));
                list.add(bidder);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Auction doesn't exist!", HttpStatus.OK);
        }
    }

    ResponseEntity<?> removeBidderFromChosenAuction(int auctionId, int bidderId)
    {
        try{
            Auction auction = auctionRepository.findById(auctionId).orElseThrow();
            List<Integer> list = auction.getBiddersIds();
            if (auction.biddersIds.size() == 0) {
                return new ResponseEntity<>("Currently this auction has no bidders", HttpStatus.OK);
            }
            if(list.contains(bidderId))
            {
                Bidder bidder = bidderRepository.findById(bidderId).orElseThrow();
                bidder.setInAuction(false);
                auction.setAuctionersNumber(auction.getAuctionersNumber()-1);
                list.remove(Integer.valueOf(bidderId));
                auctionRepository.save(auction);
                bidderRepository.save(bidder);

                return new ResponseEntity<>("Succesfully deleted bidder with ID " + bidderId + " from this auction",HttpStatus.OK);
            }
            else
        return new ResponseEntity<>("This bidder isn't in this auction",HttpStatus.OK);
        }catch(NoSuchElementException e)
        {
            return new ResponseEntity<>("Auction doesn't exist", HttpStatus.OK);
        }

    }
    ResponseEntity<?> auctionStart(int auctionId)
    {
    try{
        Random random = new Random();
        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        Bidder bidder;
        List<Integer> list = auction.getBiddersIds();

        for (Integer integer : list) {
            String n = Integer.toString(random.nextInt((50 - 1) + 1) + 1);
            bidder = bidderRepository.findById(integer).orElseThrow();
            bidder.setValueOfBid(AES.encrypt(n, bidder.getSurname()));
            bidderRepository.save(bidder);
        }



        Bidder firstBidder;
        Bidder secondBidder;
        for(int m =0;m<list.size();m++)
        {
            for(int k =0;k<list.size();k++)
            {
                if(m!=k && m<=k)
                {
                    firstBidder = bidderRepository.findById(list.get(m)).orElseThrow();
                    secondBidder = bidderRepository.findById(list.get(k)).orElseThrow();
                    millionaireProblem(firstBidder,secondBidder);
                }
            }
        }


        return new ResponseEntity<>("Auction started correctly",HttpStatus.OK);
    }
    catch(NoSuchElementException e)
    {
        return new ResponseEntity<>("Auction doesn't exist", HttpStatus.OK);
    }
    }
    private void millionaireProblem(Bidder firstBidder, Bidder secondBidder) {
        // Alicia public key (e, n) as pair
        Pairs firstBidderPairs = pairsRepository.findById(firstBidder.getBidderId()).orElseThrow();
        // Alicia: public key - e
        BigInteger firstBidderE = new BigInteger(firstBidderPairs.getE());
        // Alicia: public key - n
        BigInteger firstBidderN = new BigInteger(firstBidderPairs.getN());
        // Alicia: private key - d
        BigInteger firstBidderD = new BigInteger(AES.decrypt(firstBidder.getPrivateKey(),firstBidder.getSurname()));

        // Alicia value: I
        BigInteger firstBidderValue = new BigInteger(AES.decrypt(firstBidder.getValueOfBid(), firstBidder.getSurname()));
        // Bob value: J
        BigInteger secondBidderValue = new BigInteger(AES.decrypt(secondBidder.getValueOfBid(), secondBidder.getSurname()));

        // MAX bid
        BigInteger MAX = new BigInteger("50");

        System.out.println("Value of n:  " + firstBidderN);//todo delete
        System.out.println("Value of e:  " + firstBidderE);//todo delete
        System.out.println("Value of d:  " + firstBidderD);//todo delete
        System.out.println("Alicia value:  " + firstBidderValue);//todo delete
        System.out.println("Bob value:  " + secondBidderValue); //todo delete

        // BOB ===============================================
        BigInteger X = new BigInteger("3220");
        System.out.println("Value of X:  " + X);    //todo delete

        BigInteger C;
        C = X.modPow(firstBidderE, firstBidderN);
        System.out.println("Value of C:  " + C);    //todo delete

        BigInteger m = C.subtract(secondBidderValue).add(BigInteger.ONE);
        System.out.println("Value of m:  " + m);    //todo delete
        // ==================================================

        // ALICIA ===========================================
        // Alicia inserts MAX number of values to the list Y[]
        ArrayList<BigInteger> Y = new ArrayList<BigInteger>();
        for (BigInteger j = BigInteger.valueOf(0); j.compareTo(MAX) < 0; j = j.add(BigInteger.ONE))
        {
            BigInteger m1 = m.add(j);
            BigInteger modpow = m1.modPow(firstBidderD, firstBidderN);
            Y.add(modpow);
        }
        // if j = J, Alicia should get value of X

        ArrayList<BigInteger> Z = new ArrayList<BigInteger>();
        BigInteger P;

        while(true)
        {
            BigInteger Pt = new BigInteger(7, 1, new Random());
            if (Pt.isProbablePrime(1))
            {
                System.out.println("Value of P:  " + Pt);   //  todo delete
                P = Pt;
                break;
            }
        }

        System.out.println("Bit length of P:  " + P.bitLength());   // todo delete

        for (BigInteger j = BigInteger.valueOf(0); j.compareTo(MAX) < 0; j = j.add(BigInteger.ONE))
        {
            Z.add(Y.get(j.intValue()).mod(P));
            System.out.println(j.add(BigInteger.ONE) + "Value of Z[" + j + "]:  " + Z.get(j.intValue()));       // todo delete
        }

        ArrayList<BigInteger> W = new ArrayList<BigInteger>();
        for (BigInteger i = BigInteger.valueOf(0); i.compareTo(MAX) < 0; i = i.add(BigInteger.ONE))
        {
            int result = i.compareTo((firstBidderValue.subtract(BigInteger.ONE)));
            System.out.println("Value of RESULT:  " + result);      // todo delete
            if (result >= 0)
            {
                W.add((Z.get(i.intValue())).add(BigInteger.ONE));
            }
            else
            {
                W.add(Z.get(i.intValue()));
            }

            System.out.println(i.add(BigInteger.ONE) + "Value of W[" + i + "]:  " + W.get(i.intValue())); // todo delete
        }

        // BOB ===========================================================
        //Bob checks if W[j] === X mod P
        //if not then Alicia started incrementing values of Z[j] for indexes smaller than J
        //which means I < J, which means Bob bidded more than Alicia
        //================================================================

        BigInteger G = X.mod(P);
        System.out.println("Value of G:  " + G);    // todo delete

        int result = G.compareTo(W.get(secondBidderValue.intValue()-1));
        if (result == 0)
        {
            System.out.println("Alicia bidded more (or same)!");// todo delete
            firstBidder.setWonMillionaireProblemCount(firstBidder.getWonMillionaireProblemCount()+1);
            bidderRepository.save(firstBidder);
        }
        else
        {
            secondBidder.setWonMillionaireProblemCount(secondBidder.getWonMillionaireProblemCount()+1);
            bidderRepository.save(secondBidder);
        }
    }
}
