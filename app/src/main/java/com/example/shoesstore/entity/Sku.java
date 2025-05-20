package com.example.shoesstore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sku {
    private int id;
    private String size;
    private String color;
    private int quantity;
    private long price;
    private int productId;
} 