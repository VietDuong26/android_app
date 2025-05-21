package com.example.shoesstore.mapper;

import android.content.Context;

import com.example.shoesstore.config.cloudinary.CloudinaryManager;
import com.example.shoesstore.entity.ProductImage;
import com.example.shoesstore.model.ProductImageModel;

public class ProductImageMapper {
    private static final String TAG = "ProductImageMapper";
    private CloudinaryManager cloudinaryManager;
    private Context context;

    public ProductImageMapper(Context context) {
        this.context = context;
        this.cloudinaryManager = new CloudinaryManager();
    }

    public ProductImage toEntity(ProductImageModel model) {
        return ProductImage.builder()
                .link(model.getImageUrl())
                .productId((int) model.getProductId())
                .build();
    }
}
