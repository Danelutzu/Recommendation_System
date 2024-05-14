package com.example.backend.services;

import com.example.backend.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.backend.dtos.CategoryDTO;
import com.example.backend.dtos.ProductDTO;
import com.example.backend.dtos.builders.CategoryBuilder;
import com.example.backend.dtos.builders.ProductBuilder;
import com.example.backend.entities.Category;
import com.example.backend.entities.Product;
import com.example.backend.entities.ProductCategory;
import com.example.backend.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> findProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(ProductBuilder::toDTO).collect(Collectors.toList());
    }

    public ProductDTO findProductById(int id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            LOGGER.error("Product with id {} was not found in db", id);
            throw new ResourceNotFoundException(Product.class.getSimpleName() + " with id " + id);
        }
        return ProductBuilder.toDTO(productOptional.get());
    }

    public ProductDTO findProductByName(String name) {
        Optional<Product> productOptional = productRepository.findByName(name);
        if (!productOptional.isPresent()) {
            LOGGER.error("Product with name {} was not found in db", name);
            throw new ResourceNotFoundException(Product.class.getSimpleName() + " with name " + name);
        }
        return ProductBuilder.toDTO(productOptional.get());
    }

    public ProductDTO findProductNameWithIdByName(String name) {
        Optional<Product> productOptional = productRepository.findByName(name);
        if (!productOptional.isPresent()) {
            LOGGER.error("there is no product with the name {}", name);
            throw new ResourceNotFoundException(Product.class.getSimpleName() + " with name " + name);
        }
        Product product = productOptional.get();
        return new ProductDTO(product.getId(),product.getName(),product.getPrice(),product.getImage());
    }

    public ProductDTO findProductNameWithIdById(int id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            LOGGER.error("there is no product with the id {}", id);
            throw new ResourceNotFoundException(Product.class.getSimpleName() + " with id " + id);
        }
        Product product = productOptional.get();
        return new ProductDTO(product.getId(),product.getName(),product.getPrice(),product.getImage());
    }

    public List<ProductDTO> getAllProductNamesWithIds() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(product -> new ProductDTO(product.getId(),product.getName(),product.getPrice(),product.getImage())).collect(Collectors.toList());
    }

    public List<ProductDTO> findProductsByCategories(List<Category> categories) {
        List<Product> productList = productRepository.findAll();

        List<Product> resultProductList = new ArrayList<>();

        for (Product product : productList) {
            for (ProductCategory productCategory : product.getProductCategories()) {
                if (categories.contains(productCategory.getCategory())) {
                    resultProductList.add(product);
                    break; // No need to check more categories for this product
                }
            }
        }

        return resultProductList.stream().map(ProductBuilder::toDTO).collect(Collectors.toList());
    }


    public int insert(ProductDTO productDTO) {
        ProductDTO productDTO1 = new ProductDTO(productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getImage());
        Product product = ProductBuilder.toEntity(productDTO1);
        product = productRepository.save(product);
        LOGGER.debug("Product with id {} was inserted in db", product.getId());
        return product.getId();
    }

    public int delete(ProductDTO productDTO) {
        Product product = ProductBuilder.toEntity(productDTO);
        int idDeleted = product.getId();
        productRepository.delete(product);
        LOGGER.debug("Product with id {} was deleted from db", idDeleted);
        return idDeleted;
    }

    public int update(ProductDTO productDTO, ProductDTO updatedProductDTO) {
        Product productToUpdate = ProductBuilder.toEntity(updatedProductDTO);
        int idUpdate = productToUpdate.getId();

        // Update the properties of the product entity
        productToUpdate.setName(productDTO.getName());
        productToUpdate.setPrice(productDTO.getPrice());
        productToUpdate.setImage(productDTO.getImage());

        // Clear the existing product categories
        productToUpdate.getProductCategories().clear();

        // Assign new categories using the provided product categories list
        for (ProductCategory productCategory : productDTO.getProductCategories()) {
            // Since ProductCategory holds references to both Product and Category,
            // you can directly set the ProductCategory instance to the product's categories
            productToUpdate.getProductCategories().add(productCategory);
        }

        // Save the updated product to the database
        productRepository.save(productToUpdate);

        LOGGER.debug("Product with id {} was updated", idUpdate);
        return idUpdate;
    }


    public List<CategoryDTO> getAssignedCategoriesOfProduct(ProductDTO productDTO) {
        Product product = ProductBuilder.toEntity(productDTO);
        List<ProductCategory> productCategories = product.getProductCategories();

        return productCategories.stream()
                .map(ProductCategory::getCategory)
                .map(CategoryBuilder::toDTO)
                .collect(Collectors.toList());
    }


    public List<CategoryDTO> assignCategories(ProductDTO productDTO, List<CategoryDTO> categoryDTOList) {
        Product product = ProductBuilder.toEntity(productDTO);
        List<Category> categories = categoryDTOList.stream().map(CategoryBuilder::toEntity).toList();

        List<ProductCategory> productCategoryList = new ArrayList<>();
        for (Category category : categories) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProduct(product);
            productCategory.setCategory(category);
            productCategoryList.add(productCategory);
        }

        // Clear existing product categories and add the newly assigned ones
        product.getProductCategories().clear();
        product.getProductCategories().addAll(productCategoryList);

        // Update the product in the database
        productRepository.save(product);

        // Return the assigned categories as DTOs
        return product.getProductCategories().stream()
                .map(ProductCategory::getCategory)
                .map(CategoryBuilder::toDTO)
                .collect(Collectors.toList());
    }


    public List<ProductDTO> filterProducts(String name, Float minPrice, Float maxPrice) {
        List<Product> productList;

        if (name != null) {
            // Filter by product name (case-insensitive search)
            productList = productRepository.findByNameContainingIgnoreCase(name);
        } else {
            // Fetch all products if no name filter is provided
            productList = productRepository.findAll();
        }

        // Filter by price range if minPrice and maxPrice are provided
        if (minPrice != null && maxPrice != null) {
            productList = productList.stream()
                    .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }
        return productList.stream().map(product -> new ProductDTO(product.getId(),product.getName(),product.getPrice(),product.getImage())).collect(Collectors.toList());
//        return productList.stream().map(ProductBuilder::toDTO).collect(Collectors.toList());
    }



}
