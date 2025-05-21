package com.example.shoesstore.mapper;

import android.content.Context;

import com.example.shoesstore.dto.ProductDto;
import com.example.shoesstore.entity.Product;
import com.example.shoesstore.service.ICategoryService;
import com.example.shoesstore.service.impl.CategoryService;
import com.example.shoesstore.service.impl.ProductImageService;
import com.example.shoesstore.service.impl.SkuService;

public class ProductMapper {
    ICategoryService categoryService;

    SkuService skuService;
    ProductImageService productImageService;

    public ProductMapper(Context context) {
        this.categoryService = new CategoryService(context);
        this.productImageService = new ProductImageService(context);
        this.skuService = new SkuService(context);
    }

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryName(categoryService.findById(product.getCategoryId()).getName())
                .categoryId(product.getCategoryId())
                .productImages(productImageService.findAllByProductId(product.getId()))
                .skus(skuService.findAllByProductId(product.getId()))
                .build();
    }
}
