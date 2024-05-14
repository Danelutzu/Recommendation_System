package com.example.backend.dtos;

import com.example.backend.entities.ProductCategory;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class CategoryDTO extends RepresentationModel<CategoryDTO> {
    private int id;
    private String name;

    private List<ProductCategory> productCategories = new ArrayList<>();

    public CategoryDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
