package com.example.shoesstore.mapper;

import android.content.Context;

import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.entity.Product;
import com.example.shoesstore.service.ICategoryService;
import com.example.shoesstore.service.impl.CategoryService;
import com.example.shoesstore.service.impl.ProductImageService;

public class ProductMapper {
    ICategoryService categoryService;
    ProductImageService productImageService;
    public ProductMapper(Context context){
        this.categoryService=new CategoryService(context);
        this.productImageService=new ProductImageService(context);
    }

    public ProductDto toDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryName(categoryService.findById(product.getCategoryId()).getName())
                .productImages(productImageService.findAllByProductId(product.getId()))
                .build();
    }
}
