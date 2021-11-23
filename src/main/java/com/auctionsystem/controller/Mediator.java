package com.auctionsystem.controller;

import com.auctionsystem.bean.Item;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Mediator {

    public boolean addItem(Item item);
    public void removeItem(Item item);
    public void addAuctioneer(Auctioneer auctioneer);
    public void removeAuctioneer(Auctioneer auctioneer);
    public Set<Auctioneer> getAuctioneers();
    public Auctioneer getAuctioneerByName(String name);
    public boolean addBidderByAuctioneerName(String auctioneerName, Bidder bidder);
    public boolean addBidder(Auctioneer auctioneer, Bidder bidder);
    public boolean removeBidder(Auctioneer auctioneer, Bidder bidder);
    public Map<String, Set<Bidder>> getBiddersMap();
    public List<Item> getItems();
    public Bidder announceWinner(Auctioneer auctioneer);
}
