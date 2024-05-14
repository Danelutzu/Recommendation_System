package com.example.backend.dtos.builders;

import com.example.backend.dtos.ProductDTO;
import com.example.backend.dtos.SimpleProductDTO;
import com.example.backend.entities.Product;

public class ProductBuilder {
    public static ProductDTO toDTO(Product product){
        return new ProductDTO(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImage(),
                product.getProductCategories());
    }

    public static ProductDTO toDTOWithoutList(Product product){
        return new ProductDTO(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImage());
    }

    public static Product toEntity(ProductDTO productDTO){
        return new Product(productDTO.getId(),
                productDTO.getName(),
                productDTO.getPrice(),
                productDTO.getImage(),
                productDTO.getProductCategories());
    }

    public static Product toEntityWithoutList(ProductDTO productDTO){
        return new Product(productDTO.getId(),
                productDTO.getName(),
                productDTO.getPrice(),
                productDTO.getImage());
    }

//    public static Product toEntityWithoutList(SimpleProductDTO simpleProductDTO) {
//        return new Product(simpleProductDTO.getId(),
//                simpleProductDTO.getName(),
//                simpleProductDTO.getPrice(),
//                simpleProductDTO.getImage());
//    }
//
//
//    public static SimpleProductDTO toSimpleDTO(Product product){
//        return new SimpleProductDTO(product.getId(),
//                product.getName(),
//                product.getPrice(),
//                product.getImage());
//    }
}
