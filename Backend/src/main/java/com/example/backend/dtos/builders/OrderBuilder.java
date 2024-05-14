package com.example.backend.dtos.builders;

import com.example.backend.dtos.OrderDTO;
import com.example.backend.entities.Order;

public class OrderBuilder {

    public static OrderDTO toDTO(Order order){
        return new OrderDTO(order.getId(),
                order.getCarts(),
                order.getPrice());
    }

    public static Order toEntity(OrderDTO orderDTO){
        return new Order(orderDTO.getId(),
                orderDTO.getCarts(),
                orderDTO.getPrice());
    }
}
