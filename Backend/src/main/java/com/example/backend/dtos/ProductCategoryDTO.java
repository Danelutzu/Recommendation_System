package com.example.backend.dtos;

import com.example.backend.entities.Category;
import com.example.backend.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ProductCategoryDTO {
    private int id;

    private Product product;

    private Category category;
}
