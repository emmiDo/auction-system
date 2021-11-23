package com.auctionsystem.controller;

import com.auctionsystem.bean.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecondPriceSealedBidMediatorTest {

    @Test
    void testAddItem() {
        Mediator med = new SecondPriceSealedBidMediator();
        Item item1 = new Item("item 1", 1000, null);
        Item item2 = new Item("item 1", 1000, "Auctioneer 1");
        Item item3 = new Item("item 1", 1000, "Auctioneer 1");
        item3.setSold(true);

        boolean add_item1 = med.addItem(item1);
        boolean add_item2 = med.addItem(item2);
        boolean add_item3 = med.addItem(item3);

        assertEquals(false, add_item1);
        assertEquals(true, add_item2);
        assertEquals(false, add_item3);
    }

    @Test
    void testAddBidder() {
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

        assertEquals(1, med.getBiddersMap().size());
        assertEquals(2, med.getBiddersMap().get(a.getName()).size());
    }

    @Test
    void testAddBidderAuctionNotStarted() {
        Mediator med = new SecondPriceSealedBidMediator();

        Auctioneer a = new Auctioneer(med, "Auctioneer 1");
        Item item = new Item("item 1", 1000, a.getName());
        a.setItem(item);
        med.addAuctioneer(a);

        Bidder b1 = new Bidder(med, "John Kerry");
        Bidder b2 = new Bidder(med, "Ivone Marco");
        med.addBidder(a, b1);
        med.addBidder(a, b2);

        assertEquals(0, med.getBiddersMap().size());
    }

    @Test
    void testRemoveBidder_1() {
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

        med.removeBidder(a, b1);

        assertEquals(1, med.getBiddersMap().size());
        assertEquals(1, med.getBiddersMap().get(a.getName()).size());
    }

    @Test
    void testRemoveBidder_2() {
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

        b1.placeBid(900, a.getAuctionState());

        med.removeBidder(a, b1);

        assertEquals(1, med.getBiddersMap().size());
        assertEquals(1, med.getBiddersMap().get(a.getName()).size());
    }

    @Test
    void testRemoveBidder_3() {
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

        b1.placeBid(1000, a.getAuctionState());

        med.removeBidder(a, b1);

        assertEquals(1, med.getBiddersMap().size());
        assertEquals(2, med.getBiddersMap().get(a.getName()).size());
    }

    @Test
    void testAnnounceWinner_1() {
        Mediator med = new SecondPriceSealedBidMediator();

        Auctioneer a = new Auctioneer(med, "Auctioneer 1");
        Item item = new Item("item 1", 1000, a.getName());
        a.setItem(item);
        med.addAuctioneer(a);
        a.start();

        Bidder b1 = new Bidder(med, "John Kerry");
        Bidder b2 = new Bidder(med, "Ivone Marco");
        Bidder b3 = new Bidder(med, "Marco De Silva");

        med.addBidder(a, b1);
        med.addBidder(a, b2);
        med.addBidder(a, b3);

        b1.placeBid(1800, a.getAuctionState());
        b2.placeBid(1000, a.getAuctionState());
        b3.placeBid(1200, a.getAuctionState());

        a.terminate();
        Bidder winner = med.announceWinner(a);

        assertEquals(b1.name, winner.name);
        assertEquals(1200, winner.getWinningPrice());
    }

    @Test
    void testAnnounceWinner_2() {
        Mediator med = new SecondPriceSealedBidMediator();

        Auctioneer a = new Auctioneer(med, "Auctioneer 1");
        Item item = new Item("item 1", 1000, a.getName());
        a.setItem(item);
        med.addAuctioneer(a);
        a.start();

        Bidder b1 = new Bidder(med, "John Kerry");
        Bidder b2 = new Bidder(med, "Ivone Marco");
        Bidder b3 = new Bidder(med, "Marco De Silva");

        med.addBidder(a, b1);
        med.addBidder(a, b2);
        med.addBidder(a, b3);

        b1.placeBid(1800, a.getAuctionState());
        b2.placeBid(1800, a.getAuctionState());
        b3.placeBid(1200, a.getAuctionState());

        a.terminate();
        Bidder winner = med.announceWinner(a);

        assertEquals(1800, winner.getWinningPrice());
    }

    @Test
    void testAnnounceWinner_3() {
        Mediator med = new SecondPriceSealedBidMediator();

        Auctioneer a = new Auctioneer(med, "Auctioneer 1");
        Item item = new Item("item 1", 1000, a.getName());
        a.setItem(item);
        med.addAuctioneer(a);
        a.start();

        Bidder b1 = new Bidder(med, "John Kerry");
        med.addBidder(a, b1);

        b1.placeBid(1800, a.getAuctionState());

        a.terminate();
        Bidder winner = med.announceWinner(a);

        assertEquals(1000, winner.getWinningPrice());
    }

    @Test
    void testAnnounceWinner_4() {
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

        b1.placeBid(800, a.getAuctionState());
        b1.placeBid(900, a.getAuctionState());

        a.terminate();
        Bidder winner = med.announceWinner(a);

        assertEquals(null, winner);
    }

    @Test
    void testAnnounceWinner_5() {
        Mediator med = new SecondPriceSealedBidMediator();

        Auctioneer a = new Auctioneer(med, "Auctioneer 1");
        Item item = new Item("item 1", 1000, a.getName());
        a.setItem(item);
        med.addAuctioneer(a);
        a.start();

        Bidder b1 = new Bidder(med, "John Kerry");
        Bidder b2 = new Bidder(med, "Ivone Marco");
        Bidder b3 = new Bidder(med, "Marco De Silva");

        med.addBidder(a, b1);
        med.addBidder(a, b2);
        med.addBidder(a, b3);

        b1.placeBid(1800, a.getAuctionState());
        b2.placeBid(1800, a.getAuctionState());
        b3.placeBid(1200, a.getAuctionState());

        b1.placeBid(1750, a.getAuctionState());
        b2.placeBid(1900, a.getAuctionState());
        b3.placeBid(1100, a.getAuctionState());

        a.terminate();
        Bidder winner = med.announceWinner(a);

        assertEquals(b2.name, winner.name);
        assertEquals(1750, winner.getWinningPrice());
    }

}