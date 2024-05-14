package com.example.backend.repositories;

import com.example.backend.entities.Cart;
import com.example.backend.entities.CartHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartHistoryRepository extends JpaRepository<CartHistory,Integer> {
    @Query("SELECT c FROM CartHistory c JOIN FETCH c.product p WHERE c.user.id = :userId")
    List<CartHistory> findByUserIdWithProductWithoutCategories(@Param("userId") int userId);
}
