package com.auctionsystem.controller;

import java.util.HashMap;
import java.util.Map;

public class Bidder extends User {

    private double bid;
    private double winningPrice;
    private Map<Long, Double> bidMap = new HashMap<>();

    public Bidder() {

    }

    public Bidder(Mediator med, String name) {
        super(med, name, "User");
    }

    public Map<Long, Double> getBidMap() {
        return this.bidMap;
    }

    public double getBid() {
        return bid;
    }

    public String placeBid(double bid, State state) {
        switch (state) {
            case NOT_STARTED: return "You will have to wait!";
            case STARTED: {
                this.bid = bid;
                bidMap.put(System.currentTimeMillis(), bid);
                return "You have placed " + bid;
            }
            case TERMINATED: return "Auction is terminated";
            default: return "No operations to be allowed";
        }

    }

    public void setWinningPrice(double price) {
        this.winningPrice = price;
    }

    public double getWinningPrice() {
        return this.winningPrice;
    }

    public void leaveAuction(Auctioneer auctioneer) {
        this.mediator.removeBidder(auctioneer, this);
    }

}
