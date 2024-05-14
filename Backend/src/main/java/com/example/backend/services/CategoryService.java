package com.example.backend.services;

import com.example.backend.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.backend.dtos.CategoryDTO;
import com.example.backend.dtos.builders.CategoryBuilder;
import com.example.backend.entities.Category;
import com.example.backend.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
    }

    public List<CategoryDTO> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryBuilder::toDTO).collect(Collectors.toList());
    }

    public List<CategoryDTO> getAllCategoryNamesWithIds() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> new CategoryDTO(category.getId(), category.getName())).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryNameWithIdById(int categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()){
            LOGGER.error("Category with id {} is not found",categoryId);
            throw new ResourceNotFoundException(Category.class.getSimpleName()+" with id "+categoryId);
        }
        Category category1 = category.get();

        return new CategoryDTO(category1.getId(),category1.getName());
    }


    public CategoryDTO getCategoryById(int categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(!categoryOptional.isPresent()){
            throw new ResourceNotFoundException(Category.class.getSimpleName()+" with id " + categoryId);
        }
        return CategoryBuilder.toDTO(categoryOptional.get());
    }

    public CategoryDTO getCategoryByName(String name){
        Optional<Category> categoryOptional = categoryRepository.findCategoryByName(name);
        if(!categoryOptional.isPresent()){
            throw new ResourceNotFoundException(Category.class.getSimpleName()+" with name " + name);
        }
        return CategoryBuilder.toDTO(categoryOptional.get());
    }

    public int insert(CategoryDTO categoryDTO){
        CategoryDTO categoryDTO1 = new CategoryDTO(categoryDTO.getId(),categoryDTO.getName());
        Category category = CategoryBuilder.toEntity(categoryDTO1);
        category=categoryRepository.save(category);
        LOGGER.debug("Category with id {} was inserted",category.getId());
        return category.getId();
    }

    public int delete(CategoryDTO categoryDTO){
        Category category  =CategoryBuilder.toEntity(categoryDTO);
        int idDeleted = category.getId();
        categoryRepository.delete(category);
        LOGGER.debug("Category with id {} was deleted",idDeleted);
        return idDeleted;
    }



}
