package com.example.backend.controllers;

import com.example.backend.dtos.CartHistoryDTO;
import com.example.backend.dtos.ProductDTO;
import com.example.backend.dtos.UserDTO;
import com.example.backend.dtos.builders.ProductBuilder;
import com.example.backend.entities.Product;
import com.example.backend.services.CartHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class CartHistoryController {
    private final CartHistoryService cartHistoryService;

    @Autowired
    public CartHistoryController(CartHistoryService cartHistoryService){
        this.cartHistoryService = cartHistoryService;
    }

    @GetMapping(value = "/cartHistorys/{id}")
    public ResponseEntity<CartHistoryDTO> getCartHistoryItemById(@PathVariable("id") int cartHistoryId){
        CartHistoryDTO cartHistoryDTO = cartHistoryService.findCartHistoryById(cartHistoryId);
        return new ResponseEntity<>(cartHistoryDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/cartHistorys/byUser/{id}")
    public ResponseEntity<List<CartHistoryDTO>> getCartHistoryItemsByUserId(@PathVariable("id") int userId){
        List<CartHistoryDTO> cartHistoryDTOS = cartHistoryService.findCartHistoryByUserId(userId);
        return new ResponseEntity<>(cartHistoryDTOS,HttpStatus.OK);
    }

    @PostMapping(value = "/cartHistorys")
    public ResponseEntity<Integer> insertCartHistoryItem(@Valid @RequestBody CartHistoryDTO cartHistoryDTO){
        int cardId = cartHistoryService.insert(cartHistoryDTO);
        return new ResponseEntity<>(cardId,HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/cartHistorys/delete/{id}")
    public ResponseEntity<String> deleteCartHistoryItem(@PathVariable("id") int cartHistoryId){
        CartHistoryDTO cartHistoryDTO = cartHistoryService.findCartHistoryById(cartHistoryId);
        cartHistoryService.delete(cartHistoryDTO);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PutMapping(value = "/cartHistorys/assign/user/{idCartHistoryItemUser}")
    public ResponseEntity<UserDTO> assignUser(@PathVariable("idCartHistoryItemUser") int idCartHistoryItemUser, @RequestBody UserDTO userDTO){
        CartHistoryDTO cartHistoryDTO = cartHistoryService.findCartHistoryById(idCartHistoryItemUser);
        cartHistoryService.assignUser(cartHistoryDTO,userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @PutMapping(value = "/cartHistorys/assign/product/{idCartHistoryItemProduct}")
    public ResponseEntity<ProductDTO> assignProduct(@PathVariable("idCartHistoryItemProduct") int idCartHistoryItemProduct, @RequestBody ProductDTO productDTO){
        CartHistoryDTO cartHistoryDTO = cartHistoryService.findCartHistoryById(idCartHistoryItemProduct);
        Product assignedProduct = cartHistoryService.assignProduct(cartHistoryDTO,productDTO);
        ProductDTO assignedProductDTO = ProductBuilder.toDTOWithoutList(assignedProduct);
        cartHistoryService.assignProduct(cartHistoryDTO,productDTO);
        return new ResponseEntity<>(assignedProductDTO,HttpStatus.OK);
    }
}
