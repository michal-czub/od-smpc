package com.auctionapp.auction.modules.item;

import com.auctionapp.auction.modules.item.entity.Item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    @PostMapping("/add")
    ResponseEntity<?>addItem(@RequestBody Item newItem)
    {
        return itemService.addItem(newItem);

    }
    @GetMapping("/get-all")
    Iterable<Item> getItems()
    {
        return itemService.getItems();
    }
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteItem(@PathVariable int id)
    {
        return itemService.deleteItem(id);
    }
    @GetMapping("/{id}")
    ResponseEntity<?> getCar(@PathVariable int id)
    {
        return itemService.getItem(id);
    }
}

