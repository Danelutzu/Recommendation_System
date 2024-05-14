package com.example.backend.services;

import com.example.backend.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.backend.dtos.CartHistoryDTO;
import com.example.backend.dtos.ProductDTO;
import com.example.backend.dtos.UserDTO;
import com.example.backend.dtos.builders.CartHistoryBuilder;
import com.example.backend.dtos.builders.ProductBuilder;
import com.example.backend.dtos.builders.UserBuilder;
import com.example.backend.entities.CartHistory;
import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import com.example.backend.repositories.CartHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartHistoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartHistoryService.class);
    private final CartHistoryRepository cartHistoryRepository;

    @Autowired
    public CartHistoryService(CartHistoryRepository cartHistoryRepository) {
        this.cartHistoryRepository = cartHistoryRepository;
    }

    public List<CartHistoryDTO> findCartHistoryByUserId(int id) {
        List<CartHistory> cartHistorys = cartHistoryRepository.findByUserIdWithProductWithoutCategories(id);
        return cartHistorys.stream().map(CartHistoryBuilder::toDTO).collect(Collectors.toList());
    }

    public CartHistoryDTO findCartHistoryById(int id) {
        Optional<CartHistory> cartHistoryOptional = cartHistoryRepository.findById(id);
        if (!cartHistoryOptional.isPresent()) {
            LOGGER.error("CartHistory item with id {} was not found in db", id);
            throw new ResourceNotFoundException(CartHistory.class.getSimpleName() + " with id " + id);
        }
        return CartHistoryBuilder.toDTO(cartHistoryOptional.get());
    }

    public int insert(CartHistoryDTO cartHistoryDTO){
        CartHistory cartHistory = CartHistoryBuilder.toEntity(cartHistoryDTO);
        cartHistory = cartHistoryRepository.save(cartHistory);
        LOGGER.debug("CartHistory item with id {} was inserted in db",cartHistory.getId());
        return cartHistory.getId();
    }

    public int delete(CartHistoryDTO cartHistoryDTO){
        CartHistory cartHistory = CartHistoryBuilder.toEntity(cartHistoryDTO);
        int idDeleted = cartHistory.getId();
        cartHistoryRepository.delete(cartHistory);
        LOGGER.debug("CartHistory item with id {} was deleted from db",idDeleted);
        return idDeleted;
    }

    public int update(CartHistoryDTO cartHistoryDTO, CartHistoryDTO cartHistoryDTO1){
        CartHistory cartHistory = CartHistoryBuilder.toEntity(cartHistoryDTO1);
        int idUpdate = cartHistory.getId();
        cartHistoryDTO.setPrice(cartHistoryDTO1.getPrice());
        cartHistoryDTO.setUser(cartHistoryDTO1.getUser());
        cartHistoryDTO.setQuantity(cartHistoryDTO1.getQuantity());
        cartHistoryDTO.setProduct(cartHistoryDTO1.getProduct());

        CartHistory cartHistory1 = CartHistoryBuilder.toEntity(cartHistoryDTO);
        cartHistoryRepository.save(cartHistory1);

        LOGGER.debug("CartHistory item with id {} was updated",idUpdate);
        return idUpdate;
    }

    public User assignUser(CartHistoryDTO cartHistoryDTO, UserDTO userDTO){
        CartHistory cartHistory = CartHistoryBuilder.toEntity(cartHistoryDTO);
        User user = UserBuilder.toEntity(userDTO);

        cartHistory.setUser(user);

        CartHistoryDTO cartHistoryDTO1 = CartHistoryBuilder.toDTO(cartHistory);

//        update(cartHistoryDTO,cartHistoryDTO1);
        cartHistoryRepository.save(cartHistory);
        LOGGER.debug("The user with id {} was assigned successfully to the cartHistory item with id {}",userDTO.getId(),cartHistoryDTO1.getId());

        return user;

    }

    public Product assignProduct(CartHistoryDTO cartHistoryDTO, ProductDTO productDTO){
        CartHistory cartHistory = CartHistoryBuilder.toEntity(cartHistoryDTO);
        Product product = ProductBuilder.toEntityWithoutList(productDTO);

        cartHistory.setProduct(product);

        CartHistoryDTO cartHistoryDTO1 = CartHistoryBuilder.toDTO(cartHistory);

        update(cartHistoryDTO,cartHistoryDTO1);

        LOGGER.debug("The product with id {} was assigned successfully to the cartHistory item with id {}",productDTO.getId(),cartHistoryDTO1.getId());

        return product;
    }
}
