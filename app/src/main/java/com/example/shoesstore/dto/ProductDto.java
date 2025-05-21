package com.example.shoesstore.dto;

import com.example.shoesstore.entity.ProductImage;
import com.example.shoesstore.entity.Sku;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {
    private int id;
    private String name;
    private String description;
    private String categoryName;
    private int categoryId;
    private List<ProductImage> productImages;
    private List<Sku> skus;
}
