package com.auctionsystem.controller;

import com.auctionsystem.bean.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class SecondPriceSealedBidMediator implements Mediator {

    static final Logger log = LoggerFactory.getLogger(SecondPriceSealedBidMediator.class);
    private Map<String, Set<Bidder>> biddersMap;
    private Set<Auctioneer> auctioneers;
    private static SecondPriceSealedBidMediator INSTANCE;

    public SecondPriceSealedBidMediator() {
        this.auctioneers = new HashSet<>();
        this.biddersMap = new HashMap<>();
    }

    public static SecondPriceSealedBidMediator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SecondPriceSealedBidMediator();
        }

        return INSTANCE;
    }

    @Override
    public boolean addItem(Item item) {
        if (item != null && item.canBeSold()
                && item.getOwner() != null
                && !item.getOwner().equals("")) {
            Auctioneer auctioneer = new Auctioneer(this, item.getOwner());
            auctioneer.setItem(item);

            addAuctioneer(auctioneer);
            return true;
        }
        return false;
    }

    @Override
    public void removeItem(Item item) {
        Auctioneer auctioneer = getAuctioneerByName(item.getOwner());
        if (auctioneer != null)
            removeAuctioneer(auctioneer);
    }

    @Override
    public void addAuctioneer(Auctioneer auctioneer) {
        this.auctioneers.add(auctioneer);
    }

    @Override
    public void removeAuctioneer(Auctioneer auctioneer) {
        this.auctioneers.remove(auctioneer);
    }

    @Override
    public Set<Auctioneer> getAuctioneers() {
        return auctioneers;
    }

    @Override
    public Auctioneer getAuctioneerByName(String name) {
        List<Auctioneer> auctioneer = this.auctioneers.stream()
                .filter(a -> a.getName().equals(name))
                .collect(Collectors.toList());

        return (auctioneer.size() > 0) ? auctioneer.get(0) : null;
    }

    @Override
    public Map<String, Set<Bidder>> getBiddersMap() {
        return this.biddersMap;
    }

    @Override
    public boolean addBidderByAuctioneerName(String auctioneerName, Bidder bidder) {
        Auctioneer auctioneer = getAuctioneerByName(auctioneerName);

        if (auctioneer != null)
            return addBidder(auctioneer, bidder);

        return false;
    }

    @Override
    public boolean addBidder(Auctioneer auctioneer, Bidder bidder) {
        Set<Bidder> bidders = biddersMap.get(auctioneer.getName());

        if (bidders == null) bidders = new HashSet<>();

        if (auctioneer.getAuctionState() == State.STARTED && !bidders.contains(bidder)) {
            bidders.add(bidder);
            this.biddersMap.put(auctioneer.getName(), bidders);
            System.out.println(bidder.name + " joined the auction");
            return true;
        }
        return false;
    }

    @Override
    public boolean removeBidder(Auctioneer auctioneer, Bidder bidder) {
        Set<Bidder> bidders = biddersMap.get(auctioneer.getName());
        if (!bidders.contains(bidder)) {
            System.out.println(bidder.name + " not in the auction");
            return true;
        }

        if (bidder.getBid() >= auctioneer.getItem().getPrice()) {
            System.out.println(bidder.name + " cannot leave the auction");
            return false;
        } else {
            bidders.remove(bidder);
            this.biddersMap.put(auctioneer.getName(), bidders);
            System.out.println(bidder.name + " left the auction");
            return true;
        }
    }

    @Override
    public List<Item> getItems() {
        List<Item> items = this.auctioneers.stream()
                .filter(auctioneer -> !auctioneer.getItem().isSold())
                .map(auctioneer -> auctioneer.getItem()).collect(Collectors.toList());

        return items;
    }

    @Override
    public Bidder announceWinner(Auctioneer auctioneer) {
        Bidder winner = null;

        Set<Bidder> bidders = biddersMap.get(auctioneer.getName());

        for (Bidder b: bidders) {
            log.info(b + " bid =" + b.getBid());
        }

        if (bidders.size() > 0 && auctioneer.getAuctionState() == State.TERMINATED) {
            List<Bidder> sortedByBid = bidders.stream()
                    .sorted(Comparator.comparingDouble(Bidder::getBid).reversed())
                    .filter(bidder -> bidder.getBid() >= auctioneer.getItem().getPrice())
                    .collect(Collectors.toList());

            if (sortedByBid.size() == 0) return null;

            Bidder draftWinner = sortedByBid.get(0);

            List<Bidder> groupByBid = bidders.stream()
                    .filter(bidder -> Utility.doubleEqualCheck(draftWinner.getBid(),bidder.getBid()))
                    .collect(Collectors.toList());

            System.out.println("ReservedPrice: " + auctioneer.getItem().getPrice());

            if (sortedByBid.size() == 1) {
                winner = draftWinner;
                winner.setWinningPrice(auctioneer.getItem().getPrice());
            } else if (groupByBid.size() > 1) {
                Random random = new Random();
                int randomNo = random.nextInt(groupByBid.size());
                winner = groupByBid.get(randomNo);
                winner.setWinningPrice(winner.getBid());
            } else {
                winner = draftWinner;
                winner.setWinningPrice(sortedByBid.get(1).getBid());
            }

            auctioneer.getItem().setSold(true);
            auctioneer.setAuctionState(State.CLOSED);
            System.out.println(winner.name + " wins at the price of " + winner.getWinningPrice() + "." +
                    "/n Auction closed.");
        }
        return winner;
    }

}
