package com.example.backend.dtos.builders;

import com.example.backend.dtos.CartDTO;
import com.example.backend.entities.Cart;

public class CartBuilder {
//    public static CartDTO toDTO(Cart cart){
//        return new CartDTO(cart.getId(),
//                ProductBuilder.toSimpleDTO(cart.getProduct()),
//                cart.getUser(),
//                cart.getQuantity(),
//                cart.getPrice());
//    }
//
//    public static Cart toEntity(CartDTO cartDTO){
//        return new Cart(cartDTO.getId(),
//                ProductBuilder.toEntityWithoutList(cartDTO.getProduct()),
//                cartDTO.getUser(),
//                cartDTO.getQuantity(),
//                cartDTO.getPrice());
//    }

    public static CartDTO toDTO(Cart cart){
        return new CartDTO(cart.getId(),
                cart.getProduct(),
                cart.getUser(),
                cart.getQuantity(),
                cart.getPrice());
    }

    public static Cart toEntity(CartDTO cartDTO){
        return new Cart(cartDTO.getId(),
                cartDTO.getProduct(),
                cartDTO.getUser(),
                cartDTO.getQuantity(),
                cartDTO.getPrice());
    }

}
