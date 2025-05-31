package com.example.shoesstore.mapper;

import android.content.Context;

import com.example.shoesstore.dto.OrderItemDto;
import com.example.shoesstore.entity.OrderItem;
import com.example.shoesstore.entity.Sku;
import com.example.shoesstore.model.OrderItemModel;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.service.impl.ProductService;
import com.example.shoesstore.service.impl.SkuService;

public class OrderItemMapper {
    private SkuService skuService;
    private IProductService productService;

    public OrderItemMapper(Context context) {
        this.skuService = new SkuService(context);
        this.productService = new ProductService(context);
    }

    public OrderItem toEntity(OrderItemModel model) {
        return OrderItem.builder()
                .skuId(model.getSkuId())
                .quantity(model.getQuantity())
                .ordersId(model.getOrdersId())
                .build();
    }

    public OrderItemDto toDto(OrderItem item) {
        Sku sku = skuService.findById(item.getSkuId());
        return OrderItemDto.builder()
                .sku(sku)
                .quantity(item.getQuantity())
                .product(productService.findById(sku.getProductId()))
                .build();
    }
}
