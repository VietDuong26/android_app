package com.example.shoesstore.dto;

import java.sql.Timestamp;
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
public class OrderDto {
    private int id;
    private UserDto user;
    private String username;
    private String address;
    private String phone;
    private Timestamp createdAt;
    private int status;
    private List<OrderItemDto> items;
}
