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
public class ProductDTO extends RepresentationModel<ProductDTO> {
    private int id;
    private String name;

    private float price;

    private String image;

    private List<ProductCategory> productCategories = new ArrayList<>();

    public ProductDTO(int id, String name, float price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }
}
