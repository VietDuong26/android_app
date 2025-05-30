package com.example.shoesstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartModel {
    private int id;
    private int skuId;
    private int userId;
    private int quantity;
}
