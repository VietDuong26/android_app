package com.example.shoesstore.mapper;

import android.content.Context;

import com.example.shoesstore.dto.OrderDto;
import com.example.shoesstore.entity.Order;
import com.example.shoesstore.model.OrderModel;
import com.example.shoesstore.service.IUserService;
import com.example.shoesstore.service.impl.OrderItemService;
import com.example.shoesstore.service.impl.UserService;

public class OrderMapper {
    private IUserService userService;
    private OrderItemService orderItemService;

    public OrderMapper(Context context) {
        this.userService = new UserService(context);
        this.orderItemService = new OrderItemService(context);
    }

    public Order toEntity(OrderModel model) {
        return Order.builder()
                .address(model.getAddress())
                .phone(model.getPhone())
                .name(model.getName())
                .userId(model.getUserId())
                .build();
    }

    public OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .username(order.getName())
                .address(order.getAddress())
                .phone(order.getPhone())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .user(userService.findById(order.getUserId()))
                .items(orderItemService.getAllByOrder(order.getId()))
                .build();
    }
}
