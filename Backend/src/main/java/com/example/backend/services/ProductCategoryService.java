package com.example.backend.services;

import com.example.backend.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.backend.dtos.CategoryDTO;
import com.example.backend.dtos.ProductCategoryDTO;
import com.example.backend.dtos.ProductDTO;
import com.example.backend.dtos.builders.CategoryBuilder;
import com.example.backend.dtos.builders.ProductBuilder;
import com.example.backend.dtos.builders.ProductCategoryBuilder;
import com.example.backend.entities.Category;
import com.example.backend.entities.Product;
import com.example.backend.entities.ProductCategory;
import com.example.backend.repositories.ProductCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductCategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryService.class);

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<ProductCategoryDTO> findProductCategories() {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        return productCategories.stream().map(ProductCategoryBuilder::toDTO).collect(Collectors.toList());
    }

    public ProductCategoryDTO findProductCategoryById(int id) {
        Optional<ProductCategory> productCategories = productCategoryRepository.findById(id);
        if (!productCategories.isPresent()) {
            LOGGER.error("There is no ProductCategory with the id {}", id);
            throw new ResourceNotFoundException(ProductCategory.class.getSimpleName() + " with id " + id);
        }
        return ProductCategoryBuilder.toDTO(productCategories.get());
    }

    public List<ProductDTO> findProductsByCategoryId(int categoryId) {
        return productCategoryRepository.findProductsByCategoryId(categoryId).stream().map(ProductBuilder::toDTOWithoutList).collect(Collectors.toList());
    }

    public List<CategoryDTO> findCategoriesByProductId(int productId) {
        return productCategoryRepository.findCategoriesByProductId(productId).stream().map(CategoryBuilder::toDTOWithoutList).collect(Collectors.toList());
    }


    public ProductCategoryDTO createProductCategory(ProductCategoryDTO productCategoryDTO) {
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setProduct(productCategoryDTO.getProduct());
        productCategory1.setCategory(productCategoryDTO.getCategory());
        ProductCategory productCategory = productCategoryRepository.save(productCategory1);

        return ProductCategoryBuilder.toDTO(productCategory);
    }

    public ProductCategory updateProductCategory(int id, ProductCategory productCategory) {
        Optional<ProductCategory> productCategory1 = productCategoryRepository.findById(id);
        if (!productCategory1.isPresent()){
            LOGGER.error("there is no productCategory in the db with the id {}",id);
            throw new ResourceNotFoundException(ProductCategory.class.getSimpleName()+" with id "+id);
        }
        ProductCategory productCategory2 = productCategory1.get();
        productCategory2.setCategory(productCategory.getCategory());
        productCategory2.setProduct(productCategory.getProduct());

        return productCategoryRepository.save(productCategory2);
    }


    public void deleteProductCategory(int id) {
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(id);
        if (!productCategory.isPresent()){
            LOGGER.error("There is no productcategory with the id {}",id);
            throw new ResourceNotFoundException(ProductCategory.class.getSimpleName());
        }
        ProductCategory productCategory1 = productCategory.get();
        productCategoryRepository.delete(productCategory1);
    }
}
