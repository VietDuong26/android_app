package com.example.shoesstore.mapper;

import com.example.shoesstore.dto.CategoryDto;
import com.example.shoesstore.entity.Category;
import com.example.shoesstore.model.CategoryModel;

public class CategoryMapper {
    public CategoryDto toDto(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
    public Category toEntity(CategoryModel model){
        return Category.builder()
                .name(model.getName())
                .build();
    }
}
