package com.example.shoesstore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private int id;
    private int userId;
    private int skuId;
    private int quantity;
} 