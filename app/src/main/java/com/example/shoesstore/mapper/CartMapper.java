package com.example.shoesstore.mapper;

import android.content.Context;

import com.example.shoesstore.dto.CartDto;
import com.example.shoesstore.entity.Cart;
import com.example.shoesstore.model.CartModel;
import com.example.shoesstore.service.IProductService;
import com.example.shoesstore.service.IUserService;
import com.example.shoesstore.service.impl.ProductService;
import com.example.shoesstore.service.impl.SkuService;
import com.example.shoesstore.service.impl.UserService;

public class CartMapper {
    private IUserService userService;
    private SkuService skuService;

    private IProductService productService;

    public CartMapper(Context context) {
        this.userService = new UserService(context);
        this.skuService = new SkuService(context);
        this.productService = new ProductService(context);
    }

    public Cart toEntity(CartModel model) {
        return Cart.builder()
                .id(model.getId())
                .skuId(model.getSkuId())
                .userId(model.getUserId())
                .quantity(model.getQuantity())
                .build();
    }

    public CartDto toDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .user(userService.findById(cart.getUserId()))
                .sku(skuService.findById(cart.getSkuId()))
                .product(productService.findById(skuService.findById(cart.getSkuId()).getProductId()))
                .quantity(cart.getQuantity())
                .build();
    }
}
