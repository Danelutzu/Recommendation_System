package com.example.backend.repositories;

import com.example.backend.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.product p WHERE c.user.id = :userId")
    List<Cart> findByUserIdWithProductWithoutCategories(@Param("userId") int userId);
}
