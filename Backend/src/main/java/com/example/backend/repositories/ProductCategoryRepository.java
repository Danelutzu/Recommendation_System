package com.example.backend.repositories;

import com.example.backend.entities.Category;
import com.example.backend.entities.Product;
import com.example.backend.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
    @Query("SELECT pc.product FROM ProductCategory pc WHERE pc.category.id = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") int categoryId);

    @Query("SELECT pc.category FROM ProductCategory pc WHERE pc.product.id = :productId")
    List<Category> findCategoriesByProductId(@Param("productId") int productId);
}
