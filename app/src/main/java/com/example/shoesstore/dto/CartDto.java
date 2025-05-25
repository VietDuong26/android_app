package com.example.shoesstore.dto;

import com.example.shoesstore.entity.Sku;

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
public class CartDto {
    private int id;
    private UserDto user;
    private Sku sku;
    private int quantity;
    private ProductDto product;
}
