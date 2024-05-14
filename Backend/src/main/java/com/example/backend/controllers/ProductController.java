package com.example.backend.controllers;

import com.example.backend.dtos.CategoryDTO;
import com.example.backend.dtos.ProductDTO;
import com.example.backend.entities.Category;
import com.example.backend.entities.Product;
import com.example.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")

public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/products/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") int productId) {
        ProductDTO productDTO = productService.findProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/products/ByName/{name}")
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable("name") String name) {
        ProductDTO productDTO = productService.findProductByName(name);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/products/productNames/ByName/{name}")
    public ResponseEntity<ProductDTO> getProductNameWithIdByName(@PathVariable String name){
        ProductDTO productDTO = productService.findProductNameWithIdByName(name);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/products/productNames/ById/{id}")
    public ResponseEntity<ProductDTO> getProductNameWithIdByName(@PathVariable int id){
        ProductDTO productDTO = productService.findProductNameWithIdById(id);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/products/productNames")
    public ResponseEntity<List<ProductDTO>> getAllProductNamesWithIds(){
        List<ProductDTO> productDTOList = productService.getAllProductNamesWithIds();
        return new ResponseEntity<>(productDTOList,HttpStatus.OK);
    }

    @GetMapping(value = "/products/ByCategories")
    public ResponseEntity<List<ProductDTO>> getProductsByCategories(@Valid @RequestBody List<Category> categories) {
        List<ProductDTO> productList = productService.findProductsByCategories(categories);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping(value = "/products")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> productDTOList = productService.findProducts();
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @PostMapping(value = "/products")
    public ResponseEntity<Integer> insertProduct(@Valid @RequestBody ProductDTO productDTO) {
        int productId = productService.insert(productDTO);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/products/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int productId) {
        ProductDTO productDTO = productService.findProductById(productId);
        productService.delete(productDTO);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    @PutMapping(value = "/products/assign/{idProduct}")
    public ResponseEntity<List<CategoryDTO>> assignCategory(@PathVariable("idProduct") int idProduct, @RequestBody List<CategoryDTO> categories) {
        ProductDTO productDTO = productService.findProductById(idProduct);
        List<CategoryDTO> assignedCategories = productService.assignCategories(productDTO, categories);
        return new ResponseEntity<>(assignedCategories, HttpStatus.OK);
    }


    @GetMapping(value = "/products/assignedCategories/{idProduct}")
    public ResponseEntity<List<CategoryDTO>> getAssignedCategoriesForProduct(@PathVariable("idProduct") int idProduct) {
        ProductDTO productDTO = productService.findProductById(idProduct);
        List<CategoryDTO> categoryDTOList = productService.getAssignedCategoriesOfProduct(productDTO);
        return new ResponseEntity<>(categoryDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/products/filter")
    public ResponseEntity<List<ProductDTO>> filterProducts(@RequestParam(name = "name", required = false) String name,
                                                           @RequestParam(name = "minPrice", required = false) Float minPrice,
                                                           @RequestParam(name = "maxPrice", required = false) Float maxPrice) {
        // Use the filter criteria to fetch filtered products from the database
        List<ProductDTO> filteredProducts = productService.filterProducts(name, minPrice, maxPrice);
        return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
    }




}
