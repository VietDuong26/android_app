package com.example.shoesstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SkuModel {
    private String size;
    private String color;
    private int quantity;
    private long price;
    private int productId;
}
