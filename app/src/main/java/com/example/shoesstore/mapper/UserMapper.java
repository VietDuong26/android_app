package com.example.shoesstore.mapper;

import com.example.shoesstore.constants.Role;
import com.example.shoesstore.dto.UserDto;
import com.example.shoesstore.entity.User;
import com.example.shoesstore.model.UserModel;

public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public User toEntity(UserModel model) {
        return User.builder()
                .name(model.getName())
                .email(model.getEmail())
                .password(model.getPassword())
                .build();
    }
}
