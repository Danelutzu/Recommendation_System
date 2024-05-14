package com.example.backend.dtos.builders;

import com.example.backend.dtos.ProductCategoryDTO;
import com.example.backend.entities.ProductCategory;

public class ProductCategoryBuilder {
    public static ProductCategoryDTO toDTO(ProductCategory productCategory){
        return new ProductCategoryDTO(productCategory.getId(),
                productCategory.getProduct(),
                productCategory.getCategory());
    }

    public static ProductCategory toEntity(ProductCategoryDTO productCategoryDTO){
        return new ProductCategory(productCategoryDTO.getId(),
                productCategoryDTO.getProduct(),
                productCategoryDTO.getCategory());
    }
}
