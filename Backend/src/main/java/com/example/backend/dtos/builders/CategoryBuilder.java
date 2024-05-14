package com.example.backend.dtos.builders;

import com.example.backend.dtos.CategoryDTO;
import com.example.backend.entities.Category;

public class CategoryBuilder {
    public static CategoryDTO toDTO(Category category){
        return new CategoryDTO(category.getId(),
                category.getName(),
                category.getProductCategories());
    }

    public static CategoryDTO toDTOWithoutList(Category category){
        return new CategoryDTO(category.getId(),
                category.getName());
    }

    public static Category toEntity(CategoryDTO categoryDTO){
        return new Category(categoryDTO.getId(),
                categoryDTO.getName(),
                categoryDTO.getProductCategories());
    }
}
