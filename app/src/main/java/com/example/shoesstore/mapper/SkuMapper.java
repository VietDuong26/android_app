package com.example.shoesstore.mapper;

import com.example.shoesstore.entity.ProductImage;
import com.example.shoesstore.entity.Sku;
import com.example.shoesstore.model.SkuModel;

public class SkuMapper {
    public Sku toEntity(SkuModel model){
        return Sku.builder()
                .size(model.getSize())
                .color(model.getColor())
                .quantity(model.getQuantity())
                .price(model.getPrice())
                .productId(model.getProductId())
                .build();
    }
}
