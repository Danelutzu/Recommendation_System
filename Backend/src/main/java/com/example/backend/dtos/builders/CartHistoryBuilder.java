package com.example.backend.dtos.builders;

import com.example.backend.dtos.CartHistoryDTO;
import com.example.backend.entities.CartHistory;

public class CartHistoryBuilder {
    //    public static CartHistoryDTO toDTO(CartHistory cartHistory){
//        return new CartHistoryDTO(cartHistory.getId(),
//                ProductBuilder.toSimpleDTO(cartHistory.getProduct()),
//                cartHistory.getUser(),
//                cartHistory.getQuantity(),
//                cartHistory.getPrice());
//    }
//
//    public static CartHistory toEntity(CartHistoryDTO cartHistoryDTO){
//        return new CartHistory(cartHistoryDTO.getId(),
//                ProductBuilder.toEntityWithoutList(cartHistoryDTO.getProduct()),
//                cartHistoryDTO.getUser(),
//                cartHistoryDTO.getQuantity(),
//                cartHistoryDTO.getPrice());
//    }

    public static CartHistoryDTO toDTO(CartHistory cartHistory){
        return new CartHistoryDTO(cartHistory.getId(),
                cartHistory.getProduct(),
                cartHistory.getUser(),
                cartHistory.getQuantity(),
                cartHistory.getPrice());
    }

    public static CartHistory toEntity(CartHistoryDTO cartHistoryDTO){
        return new CartHistory(cartHistoryDTO.getId(),
                cartHistoryDTO.getProduct(),
                cartHistoryDTO.getUser(),
                cartHistoryDTO.getQuantity(),
                cartHistoryDTO.getPrice());
    }
}
