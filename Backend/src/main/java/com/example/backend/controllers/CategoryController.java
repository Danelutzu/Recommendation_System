package com.example.backend.controllers;

import com.example.backend.dtos.CategoryDTO;
import com.example.backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")

public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService=categoryService;
    }

    @GetMapping(value = "/categories/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") int categoryId){
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/categories/byName/{name}")
    public ResponseEntity<CategoryDTO> getCategoryByName(@PathVariable("name")String name){
        CategoryDTO categoryDTO = categoryService.getCategoryByName(name);
        return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<List<CategoryDTO>> getCategories(){
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryDTOList,HttpStatus.OK);
    }

    @GetMapping(value = "/categories/categoryNames")
    public ResponseEntity<List<CategoryDTO>> getCategoryNamesWithIds() {
        List<CategoryDTO> categoryNamesWithIds = categoryService.getAllCategoryNamesWithIds();
        return new ResponseEntity<>(categoryNamesWithIds, HttpStatus.OK);
    }

    @GetMapping(value = "/categories/categoryNames/byId/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryNameWithIdById(@PathVariable int categoryId){
        CategoryDTO categoryDTO = categoryService.getCategoryNameWithIdById(categoryId);
        return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
    }


    @PostMapping(value = "/categories")
    public ResponseEntity<Integer> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        int categoryId = categoryService.insert(categoryDTO);
        return new ResponseEntity<>(categoryId,HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/categories/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") int categoryId){
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        categoryService.delete(categoryDTO);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }


}
