package com.example.backend.services;

import com.example.backend.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.backend.dtos.CartDTO;
import com.example.backend.dtos.ProductDTO;
import com.example.backend.dtos.UserDTO;
import com.example.backend.dtos.builders.CartBuilder;
import com.example.backend.dtos.builders.ProductBuilder;
import com.example.backend.dtos.builders.UserBuilder;
import com.example.backend.entities.Cart;
import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import com.example.backend.repositories.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<CartDTO> findCartByUserId(int id) {
        List<Cart> carts = cartRepository.findByUserIdWithProductWithoutCategories(id);
        return carts.stream().map(CartBuilder::toDTO).collect(Collectors.toList());
    }

    public CartDTO findCartById(int id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (!cartOptional.isPresent()) {
            LOGGER.error("Cart item with id {} was not found in db", id);
            throw new ResourceNotFoundException(Cart.class.getSimpleName() + " with id " + id);
        }
        return CartBuilder.toDTO(cartOptional.get());
    }

    public int insert(CartDTO cartDTO){
        Cart cart = CartBuilder.toEntity(cartDTO);
        cart = cartRepository.save(cart);
        LOGGER.debug("Cart item with id {} was inserted in db",cart.getId());
        return cart.getId();
    }

    public int delete(CartDTO cartDTO){
        Cart cart = CartBuilder.toEntity(cartDTO);
        int idDeleted = cart.getId();
        cartRepository.delete(cart);
        LOGGER.debug("Cart item with id {} was deleted from db",idDeleted);
        return idDeleted;
    }

    public int update(CartDTO cartDTO, CartDTO cartDTO1){
        Cart cart = CartBuilder.toEntity(cartDTO1);
        int idUpdate = cart.getId();
        cartDTO.setPrice(cartDTO1.getPrice());
        cartDTO.setUser(cartDTO1.getUser());
        cartDTO.setQuantity(cartDTO1.getQuantity());
        cartDTO.setProduct(cartDTO1.getProduct());

        Cart cart1 = CartBuilder.toEntity(cartDTO);
        cartRepository.save(cart1);

        LOGGER.debug("Cart item with id {} was updated",idUpdate);
        return idUpdate;
    }

    public User assignUser(CartDTO cartDTO, UserDTO userDTO){
        Cart cart = CartBuilder.toEntity(cartDTO);
        User user = UserBuilder.toEntity(userDTO);

        cart.setUser(user);

        CartDTO cartDTO1 = CartBuilder.toDTO(cart);

//        update(cartDTO,cartDTO1);
        cartRepository.save(cart);
        LOGGER.debug("The user with id {} was assigned successfully to the cart item with id {}",userDTO.getId(),cartDTO1.getId());

        return user;

    }

    public Product assignProduct(CartDTO cartDTO, ProductDTO productDTO){
        Cart cart = CartBuilder.toEntity(cartDTO);
        Product product = ProductBuilder.toEntityWithoutList(productDTO);

        cart.setProduct(product);

        CartDTO cartDTO1 = CartBuilder.toDTO(cart);

        update(cartDTO,cartDTO1);

        LOGGER.debug("The product with id {} was assigned successfully to the cart item with id {}",productDTO.getId(),cartDTO1.getId());

        return product;
    }




}
