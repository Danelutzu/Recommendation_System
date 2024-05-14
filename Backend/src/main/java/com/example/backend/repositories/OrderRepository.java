package com.example.backend.repositories;

import com.example.backend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    @Query("SELECT o.id, p.name " +
            "FROM Order o " +
            "JOIN o.carts c " +
            "JOIN c.product p " +
            "WHERE c.user.id = :userId " +
            "AND o.id = (" +
            "    SELECT MAX(o2.id) " +
            "    FROM Order o2 " +
            "    JOIN o2.carts c2 " +
            "    WHERE c2.user.id = :userId" +
            ")")
    List<Object[]> findLastOrderProductsByUserId(@Param("userId") Integer userId);

}
