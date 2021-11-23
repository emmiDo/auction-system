package com.auctionsystem.service;

import com.auctionsystem.controller.Bidder;
import com.auctionsystem.controller.Mediator;
import com.auctionsystem.controller.SecondPriceSealedBidMediator;
import com.auctionsystem.controller.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//TODO- This class is created for the purpose of UI
@Controller
@RequestMapping("/bidding")
public class BiddingService {

    static final Logger log = LoggerFactory.getLogger(BiddingService.class);
    private Mediator mediator = SecondPriceSealedBidMediator.getInstance();

    @RequestMapping("/join_auction/{auctioneer}")
    public String viewJoinAuction(@PathVariable("auctioneer") String auctioneerName, Model model) {
        model.addAttribute("url", "/bidding/join_confirmation/" + auctioneerName);
        model.addAttribute("bidder", new Bidder());
        return "join_auction";
    }

    @RequestMapping("/join_confirmation/{auctioneer}")
    public String viewJoinConfirmation(@PathVariable("auctioneer") String auctioneerName,
                                       @ModelAttribute Bidder bidder, Model model) {
        model.addAttribute("url",
                "/auction/addbidder/"+auctioneerName+'/'+bidder.getName());
        return "join_confirmation";
    }

    @RequestMapping("/place_bid/{auctioneer}/{bidder}")
    public String viewPlaceBid(@PathVariable("auctioneer") String auctioneerName,
                               @PathVariable("bidder") String bidderName) {
        return "place_bid";
    }

}
