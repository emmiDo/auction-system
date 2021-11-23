# Technology stack

* Java 11
* Spring boot 2

# Run service

Before building the project, change value for`server.port`property in`src/main/resources/application.properties`file (if needed). Default value is `8080`. To build the project, run below command to build:

`mvn clean install`

Then run below command to start service

`mvnw spring-boot:run`

# API
1.	Add Item and auctioneer
      * Description: auctioneer to add an item to the system.
      * URL: http://localhost:8080/auction/additem
      * Request parameters:
         1. item_name (string) – name of an item
         2. price (double) – price of an item (it is considered as reserved price)
         3. owner (string) - auctioneer’s name
2.	Get all items
      * Description: Get all unsold items posted for selling
      * URL: http://localhost:8080/auction/allitems
      * Request parameters: N/A
3.	Start auction
      * Description: Auctioneer to start an auction.
      * URL: http://localhost:8080/auction/start
      * Request parameters:
        1. auctioneer (string) – auctioneer’s name
4.	Add bidder 
      * Description: Add a bidder to an auction. A bidder can be added to an auction only after the auction is started.
      * URL: http://localhost:8080/auction/addbidder
      * Request parameters
        1. auctioneer (string) – name of the auctioneer who is selling the item
        2. name (string) – bidder’s name who joins in the auction
5.	Place bidder
      * Description: place bid by a bidder for an item
      * URL: http://localhost:8080/auction/placebid
      * Request parameters
        1. auctioneer (string) – name of the auctioneer who is selling the item
        2. bidder (string) – name of the bidder who is placing a bid
        3. bid (double) – bid amount
6.	All bidder
      * Description: get list of names of bidders who joined in an auction
      * URL: http://localhost:8080/auction/allbidders
      * Request parameters:
        1. auctioneer (string) – name of the auctioneer
7.	Terminate auction
      * Description: Terminate an auction
      * URL: http://localhost:8080/auction/terminate
      * Request parameters:
        1. auctioneer (string) – name of the auctioneer who is terminating the auction
8.	Announce winner
      * Description: Announcing the winner after terminating the auction
      * URL: http://localhost:8080/auction/announcewinner
      * Request parameters:
        1. auctioneer (string) – name of the auctioneer


