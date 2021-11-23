package com.auctionsystem.controller;

import com.auctionsystem.bean.Item;

public class Auctioneer extends User {

    private Item item;
    private State auctionState = State.NOT_STARTED;

    public Auctioneer () {

    }

    public Auctioneer(Mediator mediator, String name) {
        super(mediator, name, "Auctioneer");
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public State getAuctionState() {
        return auctionState;
    }

    public void setAuctionState(State auctionState) {
        this.auctionState = auctionState;
    }

    public void start() {
        if (this.item != null && this.item.canBeSold()) {
            this.auctionState = State.STARTED;
        }
    }

    public void terminate() {
        this.getItem().setSold(true);
        this.auctionState = State.TERMINATED;
    }
}
