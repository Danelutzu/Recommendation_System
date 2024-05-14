package com.example.backend.dtos;

import com.example.backend.entities.Cart;
import com.example.backend.entities.CartHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;


import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDTO extends RepresentationModel<OrderDTO> {
    private int id;

    private List<CartHistory> carts = new ArrayList<>();

    private float price;
}
