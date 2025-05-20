package com.example.shoesstore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int id;
    private int userId;
    private Timestamp createdAt;
    private int status;
    private String note;
    private String address;
} 