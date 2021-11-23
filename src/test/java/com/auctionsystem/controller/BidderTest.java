package com.auctionsystem.controller;

import com.auctionsystem.bean.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BidderTest {

    @Test
    void placeBid() {
        Mediator med = new SecondPriceSealedBidMediator();
        Auctioneer a = new Auctioneer(med, "Auctioneer 1");
        Item item = new Item("item 1", 1000, a.getName());

        a.setItem(item);
        med.addAuctioneer(a);
        a.start();

        Bidder b1 = new Bidder(med, "John Kerry");
        Bidder b2 = new Bidder(med, "Ivone Marco");

        med.addBidder(a, b1);
        med.addBidder(a, b2);

        b2.placeBid(1101, a.getAuctionState());

        a.terminate();

        b1.placeBid(1800, a.getAuctionState());

        assertEquals(0.00, b1.getBid());
        assertEquals(1101.00, b2.getBid());
    }
}