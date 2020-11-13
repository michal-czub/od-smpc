package com.auctionapp.auction.modules.item;

import com.auctionapp.auction.modules.item.entity.Item;
import lombok.RequiredArgsConstructor;;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    Iterable<Item> getItems()
    {
        return  itemRepository.findAll();
    }
    ResponseEntity<?> addItem(Item item)
    {
        item.setValue(item.getValue()*1000000);
        itemRepository.save(item);
        if(item.getYear()>2020)
        {
            deleteItem(item.getItemId());
            return new ResponseEntity<>("This kind of object cannot exist",HttpStatus.OK);

        }
        else if(item.getValue() > 50000000 || item.getValue()<1000000)
        {
            deleteItem(item.getItemId());
            return new ResponseEntity<>("This kind of object cannot exist, value must be between 1 and 50",HttpStatus.OK);
        }


            return new ResponseEntity<>(item,HttpStatus.OK);


    }
    ResponseEntity<String> deleteItem(int id){
        try{
            Item item = itemRepository.findById(id).orElseThrow();
            if(item.isBeingAuctioned())
            {
                return new ResponseEntity<>("This item is currently being auctioned",HttpStatus.OK);
            }
            itemRepository.deleteById(id);
            return new ResponseEntity<>("Item with this ID was deleted", HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>("Item with this ID doesn't exist", HttpStatus.OK);
        }
    }
    ResponseEntity<?> getItem(int id)
    {
        try{
            Item item = itemRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(item,HttpStatus.OK);
        }
        catch(NoSuchElementException e)
        {
            return new ResponseEntity<>("Item with this ID doesn't exist",HttpStatus.NOT_FOUND);
        }
    }
}
