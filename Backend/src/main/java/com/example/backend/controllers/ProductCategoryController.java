package com.example.backend.controllers;

import com.example.backend.dtos.CategoryDTO;
import com.example.backend.dtos.ProductCategoryDTO;
import com.example.backend.dtos.ProductDTO;
import com.example.backend.dtos.builders.ProductCategoryBuilder;
import com.example.backend.entities.Product;
import com.example.backend.entities.ProductCategory;
import com.example.backend.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping(value = "/productcategory")
    public ResponseEntity<List<ProductCategory>> getProductCategories() {
        List<ProductCategoryDTO> productCategories = productCategoryService.findProductCategories();
        return new ResponseEntity<>(productCategories.stream().map(ProductCategoryBuilder::toEntity).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/productcategory/{id}")
    public ResponseEntity<ProductCategoryDTO> getProductCategoryById(@PathVariable int id) {
        ProductCategoryDTO productCategoryDTO = productCategoryService.findProductCategoryById(id);
        return new ResponseEntity<>(productCategoryDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/products/byCategory/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable int categoryId) {
        List<ProductDTO> productsByCategoryId = productCategoryService.findProductsByCategoryId(categoryId);
        return new ResponseEntity<>(productsByCategoryId, HttpStatus.OK);
    }

    @GetMapping(value = "/categories/byProduct/{productId}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByProduct(@PathVariable int productId) {
        List<CategoryDTO> categoryDTOList = productCategoryService.findCategoriesByProductId(productId);
        return new ResponseEntity<>(categoryDTOList, HttpStatus.OK);
    }

    @PostMapping(value = "/productcategory")
    public ResponseEntity<ProductCategoryDTO> createProductCategory(@RequestBody ProductCategoryDTO productCategoryDTO) {
        ProductCategoryDTO productCategory1 = productCategoryService.createProductCategory(productCategoryDTO);
        return new ResponseEntity<>(productCategory1, HttpStatus.CREATED);
    }

    @PutMapping(value = "/productcategory/{id}")
    public ResponseEntity<ProductCategory> updateProductCategory(@PathVariable("id") int id, @RequestBody ProductCategory productCategory) {
        ProductCategory productCategory1 = productCategoryService.updateProductCategory(id, productCategory);
        return new ResponseEntity<>(productCategory1, HttpStatus.OK);
    }

    @DeleteMapping(value = "/productcategory/{id}")
    public ResponseEntity<String> deleteProductCategory(@PathVariable("id") int id) {
        productCategoryService.deleteProductCategory(id);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }


}
