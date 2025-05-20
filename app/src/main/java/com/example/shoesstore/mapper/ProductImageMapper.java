package com.example.shoesstore.mapper;

import com.example.shoesstore.entity.ProductImage;
import com.example.shoesstore.model.ProductImageModel;

public class ProductImageMapper {
    public ProductImage toEntity(ProductImageModel model){
        return ProductImage.builder()
                .link(model.getImageLink())
                .productId((int) model.getProductId())
                .build();
    }
}
