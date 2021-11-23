package com.auctionsystem.bean;

import java.util.Objects;

public class Item {

    private String name;
    private double price;
    private boolean sold;
    private String owner;

    public Item () {}

    public Item (String name, double price, String owner) {
        this.name = name;
        this.price = price;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean canBeSold() {
        if ((name != null && !name.equals(""))
                && (price > 0) && !isSold())
            return true;

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(item.getPrice(), getPrice()) == 0 &&
                Objects.equals(getName(), item.getName()) &&
                Objects.equals(getOwner(), item.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getOwner());
    }
}
