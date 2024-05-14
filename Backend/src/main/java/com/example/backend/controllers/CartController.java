package com.example.backend.controllers;

import com.example.backend.dtos.CartDTO;
import com.example.backend.dtos.ProductDTO;
import com.example.backend.dtos.UserDTO;
import com.example.backend.dtos.builders.ProductBuilder;
import com.example.backend.entities.Product;
import com.example.backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @GetMapping(value = "/carts/{id}")
    public ResponseEntity<CartDTO> getCartItemById(@PathVariable("id") int cartId){
        CartDTO cartDTO = cartService.findCartById(cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/carts/byUser/{id}")
    public ResponseEntity<List<CartDTO>> getCartItemsByUserId(@PathVariable("id") int userId){
        List<CartDTO> cartDTOS = cartService.findCartByUserId(userId);
        return new ResponseEntity<>(cartDTOS,HttpStatus.OK);
    }

    @PostMapping(value = "/carts")
    public ResponseEntity<Integer> insertCartItem(@Valid @RequestBody CartDTO cartDTO){
        int cardId = cartService.insert(cartDTO);
        return new ResponseEntity<>(cardId,HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/carts/delete/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable("id") int cartId){
        CartDTO cartDTO = cartService.findCartById(cartId);
        cartService.delete(cartDTO);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PutMapping(value = "/carts/assign/user/{idCartItemUser}")
    public ResponseEntity<UserDTO> assignUser(@PathVariable("idCartItemUser") int idCartItemUser, @RequestBody UserDTO userDTO){
        CartDTO cartDTO = cartService.findCartById(idCartItemUser);
        cartService.assignUser(cartDTO,userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @PutMapping(value = "/carts/assign/product/{idCartItemProduct}")
    public ResponseEntity<ProductDTO> assignProduct(@PathVariable("idCartItemProduct") int idCartItemProduct, @RequestBody ProductDTO productDTO){
        CartDTO cartDTO = cartService.findCartById(idCartItemProduct);
        Product assignedProduct = cartService.assignProduct(cartDTO,productDTO);
        ProductDTO assignedProductDTO = ProductBuilder.toDTOWithoutList(assignedProduct);
        cartService.assignProduct(cartDTO,productDTO);
        return new ResponseEntity<>(assignedProductDTO,HttpStatus.OK);
    }
}
