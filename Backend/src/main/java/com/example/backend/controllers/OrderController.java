package com.example.backend.controllers;

import com.example.backend.dtos.CartHistoryDTO;
import com.example.backend.dtos.OrderDTO;
import com.example.backend.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("id") int orderId){
        OrderDTO orderDTO = orderService.findOrderById(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/orders/byUser/{id}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable("id") int userId){
        List<OrderDTO> orders = orderService.findOrdersByUserId(userId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @GetMapping(value = "/orders")
    public ResponseEntity<List<OrderDTO>> getOrders(){
        List<OrderDTO> orderDTOS = orderService.findOrders();
        return new ResponseEntity<>(orderDTOS,HttpStatus.OK);
    }

    @PostMapping(value = "/orders")
    public ResponseEntity<Integer> insertOrder(@Valid @RequestBody OrderDTO orderDTO){
        int orderId = orderService.insert(orderDTO);
        return new ResponseEntity<>(orderId,HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/orders/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") int deleteOrderId){
        OrderDTO orderDTO = orderService.findOrderById(deleteOrderId);
        orderService.delete(orderDTO);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PutMapping(value = "/orders/assign/{idOrder}")
    public ResponseEntity<List<CartHistoryDTO>> assignCartItems(@PathVariable("idOrder") int assignOrderId, @RequestBody List<CartHistoryDTO> cartDTOList){
        OrderDTO orderDTO = orderService.findOrderById(assignOrderId);

        orderService.assignCartItems(orderDTO,cartDTOList);
        return new ResponseEntity<>(cartDTOList,HttpStatus.OK);
    }

    @GetMapping(value = "/orders/lastOrderProducts/{userId}")
    public ResponseEntity<List<Object[]>> getLastOrderProducts(@PathVariable int userId) {
        List<Object[]> lastOrderProducts = orderService.getLastOrderProductsForUser(userId);
        return ResponseEntity.ok(lastOrderProducts);
    }
}
