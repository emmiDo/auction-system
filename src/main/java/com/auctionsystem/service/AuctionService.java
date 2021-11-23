package com.auctionsystem.service;

import com.auctionsystem.bean.Item;
import com.auctionsystem.controller.Auctioneer;
import com.auctionsystem.controller.Bidder;
import com.auctionsystem.controller.Mediator;
import com.auctionsystem.controller.SecondPriceSealedBidMediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auction")
public class AuctionService {

    static final Logger log = LoggerFactory.getLogger(AuctionService.class);

    private Mediator mediator = SecondPriceSealedBidMediator.getInstance();

    @PostMapping("/additem")
    public ResponseEntity<String> addItem(@RequestParam("item_name") String item_name,
                                          @RequestParam("price") double price,
                                          @RequestParam("owner") String owner) {
        this.mediator.addItem(new Item(item_name, price, owner));
        return new ResponseEntity<>("Item " + item_name + " is added!", HttpStatus.OK);
    }

    @PostMapping("/start")
    public ResponseEntity<String> startAuction(@RequestParam("auctioneer") String auctioneerName) {
        Auctioneer auctioneer = this.mediator.getAuctioneerByName(auctioneerName);

        if (auctioneer == null) {
            return new ResponseEntity<>("Auctionneer is not available!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        auctioneer.start();

        return new ResponseEntity<>("Auction started!", HttpStatus.OK);
    }

    @PostMapping("/terminate")
    public ResponseEntity<String> terminateAuction(@RequestParam("auctioneer") String auctioneerName) {
        Auctioneer auctioneer = this.mediator.getAuctioneerByName(auctioneerName);

        if (auctioneer == null) {
            return new ResponseEntity<>("Auctionneer is not available!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        auctioneer.terminate();

        return new ResponseEntity<>("Auction ended!", HttpStatus.OK);
    }

    @GetMapping("/allitems")
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(this.mediator.getItems(), HttpStatus.OK);
    }

    @GetMapping("/allbidders")
    public ResponseEntity<List<String>> getAllBidders(@RequestParam("auctioneer") String auctioneerName) {
        Set<Bidder> bidderSet = this.mediator.getBiddersMap().get(auctioneerName);

        if (bidderSet == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<String> bidders = bidderSet.stream()
                .map(bidder -> bidder.getName())
                .collect(Collectors.toList());

        log.info(bidders+"");

        return new ResponseEntity<>(bidders, HttpStatus.OK);
    }

    @PostMapping("/addbidder")
    public ResponseEntity<String> addBidder(@RequestParam("auctioneer") String auctioneerName,
                                            @RequestParam("name") String bidderName) {
        if (bidderName != null && !bidderName.equals("")) {
            boolean joined = this.mediator.addBidderByAuctioneerName(auctioneerName, new Bidder(this.mediator, bidderName));

            return joined ? new ResponseEntity<>(bidderName + " joined", HttpStatus.OK) :
                    new ResponseEntity<>(bidderName + " could not join", HttpStatus.FAILED_DEPENDENCY);
        }

        return new ResponseEntity<>(bidderName + " cannot join now", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/placebid")
    public ResponseEntity<String> placeBid(@RequestParam("auctioneer") String auctioneerName,
                                           @RequestParam("bidder") String bidderName,
                                           @RequestParam("bid") double bid) {
        Auctioneer auctioneer = this.mediator.getAuctioneerByName(auctioneerName);

        if (auctioneer == null) {
            return new ResponseEntity<>("Auctionneer is not available!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Set<Bidder> bidderSet = this.mediator.getBiddersMap().get(auctioneerName);

        if (bidderSet == null) {
            return new ResponseEntity<>("No bidders is not available!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<Bidder> bidders = bidderSet.stream()
                .filter(b -> b.getName().equals(bidderName)).collect(Collectors.toList());
        if (bidders.size() == 0)
            return new ResponseEntity<>(bidderName + " did not join auction",
                    HttpStatus.INTERNAL_SERVER_ERROR);

        String results = bidders.get(0).placeBid(bid, auctioneer.getAuctionState());

        return new ResponseEntity<>(results,
                HttpStatus.OK);
    }

    @PostMapping("/announcewinner")
    public ResponseEntity<String> announceWinner(@RequestParam("auctioneer") String auctioneerName) {
        Auctioneer auctioneer = this.mediator.getAuctioneerByName(auctioneerName);

        if (auctioneer == null) {
            return new ResponseEntity<>("Auctionneer is not available!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Bidder winner = this.mediator.announceWinner(auctioneer);

        if (winner != null)
            return new ResponseEntity<>(
                    winner.getName() + " is winning at price " + winner.getWinningPrice(),
                    HttpStatus.OK
            );

        return new ResponseEntity<>("Auction was not successful.", HttpStatus.CREATED);
    }

}
