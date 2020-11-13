# SMPC
App handling auctions for the purpose of OD project (REST API)

Works on 127.0.0.1:8080

## Technologies
Project is created with:
 * Spring Boot version 2.3.1
 * PostgreSQL

## Item endpoints:
```
127.0.0.1:8080/item/get-all - get list of all items
127.0.0.1:8080/item/{id} - get item by id
127.0.0.1:8080/item/add - add new item in JSON format {name, year, value}
127.0.1.1:8000/item/delete/{id} - delete an item
```
## Bidder endpoints:
```
127.0.0.1:8080/bidder/get-all - get list of all bidders
127.0.0.1:8080/bidder/{id} - get bidder by id
127.0.0.1:8080/bidder/add - add new bidder in JSON format {firstName, surname}
127.0.1.1:8000/bidder/delete/{id} - delete a bidder
```
## Auction endpoints:
```
127.0.0.1:8080/auction/get-all - get list of all ongoing auctions
127.0.0.1:8080/auction/add - add new auction for specific item in JSON format {item : "id_of_an_item"}
127.0.0.1:8080/auction/{id} - get auction by id
127.0.0.1:8080/auction/delete/{id} - delete an auction
127.0.0.1:8080/auction/add-bidder/{auctionId}/{bidderId} - add a bidder to an auction
127.0.0.1:8080/auction/delete-bidder/{auctionId}/{bidderId} - delete a bidder from an auction
```
## Pairs endpoints:
```
127.0.0.1:8080/pairs/get-all - get pair of public RSA keys of all bidders
127.0.0.1:8080/auction/{id} - get pair by id (id of pair = id of bidder)
```
## Setup
```
To run this project install Docker Engine and Docker Compose then:
$ cd rentcar/src/main/resources
$ sudo docker-compose up
```
