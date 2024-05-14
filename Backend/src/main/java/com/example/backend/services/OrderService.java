package com.example.backend.services;

import com.example.backend.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.backend.dtos.CartDTO;
import com.example.backend.dtos.CartHistoryDTO;
import com.example.backend.dtos.OrderDTO;
import com.example.backend.dtos.builders.CartBuilder;
import com.example.backend.dtos.builders.CartHistoryBuilder;
import com.example.backend.dtos.builders.OrderBuilder;
import com.example.backend.entities.Cart;
import com.example.backend.entities.CartHistory;
import com.example.backend.entities.Order;
import com.example.backend.entities.User;
import com.example.backend.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDTO findOrderById(int id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            LOGGER.error("Order with id {} was not found in db", id);
            throw new ResourceNotFoundException(Order.class.getSimpleName() + " with id " + id);
        }
        return OrderBuilder.toDTO(orderOptional.get());
    }

    public List<OrderDTO> findOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(OrderBuilder::toDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> findOrdersByUserId(int idUser) {
        List<Order> orders = orderRepository.findAll();

        List<OrderDTO> filteredOrders = new ArrayList<>();

        for (Order order : orders) {
            List<CartHistory> carts = order.getCarts();
            boolean hasCartForUser = false;

            for (CartHistory cart : carts){
                User user = cart.getUser();
                if (user!=null && user.getId() == idUser){
                    hasCartForUser = true;
                    break;
                }
            }
            if (hasCartForUser){
                OrderDTO orderDTO = OrderBuilder.toDTO(order);
                filteredOrders.add(orderDTO);
            }
        }
        return filteredOrders;
    }

    public int insert(OrderDTO orderDTO){
        Order order = OrderBuilder.toEntity(orderDTO);
        order = orderRepository.save(order);
        LOGGER.debug("Order with id {} was inserted in db",order.getId());
        return order.getId();
    }

    public int delete(OrderDTO orderDTO){
        Order order = OrderBuilder.toEntity(orderDTO);
        int idDeleted = order.getId();
        orderRepository.delete(order);
        LOGGER.debug("Prder with id {} was deleted from db",idDeleted);
        return idDeleted;
    }

    public int update(OrderDTO orderDTO,OrderDTO orderDTO1){
        Order order = OrderBuilder.toEntity(orderDTO1);
        int idUpdate = order.getId();

        orderDTO.setCarts(orderDTO1.getCarts());
        orderDTO.setPrice(orderDTO1.getPrice());

        Order order1 = OrderBuilder.toEntity(orderDTO);
        orderRepository.save(order1);

        LOGGER.debug("Order with id {} was updated",idUpdate);
        return idUpdate;
    }

    public List<CartHistory> assignCartItems(OrderDTO orderDTO, List<CartHistoryDTO> cartDTOList){
        Order order = OrderBuilder.toEntity(orderDTO);

        List<CartHistory> carts = cartDTOList.stream().map(CartHistoryBuilder::toEntity).toList();

        order.setCarts(carts);

        OrderDTO orderDTO1 = OrderBuilder.toDTO(order);

        update(orderDTO,orderDTO1);

        LOGGER.debug("The cart items were successfully assigned to the order with id {}",orderDTO.getId());

        return carts;
    }

    public List<Object[]> getLastOrderProductsForUser(int userId) {
        return orderRepository.findLastOrderProductsByUserId(userId);
    }


}
