package com.auctionsystem.service;

import com.auctionsystem.bean.Item;
import com.auctionsystem.controller.Auctioneer;
import com.auctionsystem.controller.Bidder;
import com.auctionsystem.controller.Mediator;
import com.auctionsystem.controller.SecondPriceSealedBidMediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//TODO- This class is created for the purpose of UI
@Controller
public class Service<owner> {

    static final Logger log = LoggerFactory.getLogger(Service.class);

    private Mediator mediator = SecondPriceSealedBidMediator.getInstance();

    @RequestMapping("/home")
    public String viewHome() {
        return "home";
    }

    @RequestMapping(value = "/item_list", method = RequestMethod.GET)
    public String viewItems(Model model) {
        model.addAttribute("items", this.mediator.getItems());
        return "item_list";
    }

    @RequestMapping("/post_item")
    public String viewPostItem(Model model) {
        model.addAttribute("item", new Item());
        return "post_item";
    }

    @RequestMapping("/item")
    public String viewItem(Item item){
        this.mediator.addItem(item);
        return "item";
    }

}
