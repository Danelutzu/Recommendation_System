package com.example.backend.repositories;

import com.example.backend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Optional<Product> findByName(String name);

    List<Product> findByNameContainingIgnoreCase(String name);

}
