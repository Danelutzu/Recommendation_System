package com.example.backend.dtos;

import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartHistoryDTO extends RepresentationModel<CartHistoryDTO> {
    private int id;
    private Product product;
    private User user;
    private int quantity;
    private float price;
}
