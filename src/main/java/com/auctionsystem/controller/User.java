package com.auctionsystem.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope("session")
public class User {

    protected Mediator mediator = SecondPriceSealedBidMediator.getInstance();
    protected String name;
    protected String role;

    public User() {

    }

    public User(Mediator mediator, String name, String role) {
        this.mediator = mediator;
        this.name = name;
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(mediator, user.mediator) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediator, getName(), role);
    }

    @Override
    public String toString() {
        return "name=" + name + ", role=" + role;
    }
}
