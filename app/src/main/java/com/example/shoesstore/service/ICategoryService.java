package com.example.shoesstore.service;

import com.example.shoesstore.dto.CategoryDto;
import com.example.shoesstore.model.CategoryModel;

import java.util.List;

public interface ICategoryService extends IBaseService<CategoryModel, CategoryDto,Integer>{
    List<CategoryDto> findByName(String name);
}
