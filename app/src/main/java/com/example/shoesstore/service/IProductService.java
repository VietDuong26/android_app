package com.example.shoesstore.service;

import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.model.ProductModel;

import java.util.List;

public interface IProductService extends IBaseService<ProductModel, ProductDto, Integer> {
    List<ProductDto> getAllProductByCategoryId(int categoryId);
}
