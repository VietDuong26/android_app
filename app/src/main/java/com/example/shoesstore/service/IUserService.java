package com.example.shoesstore.service;

import com.example.shoesstore.dto.UserDto;
import com.example.shoesstore.model.UserModel;

import java.util.List;

public interface IUserService extends IBaseService<UserModel, UserDto,Integer>{
    UserDto login(String username,String password);
    List<UserDto> findByName(String name);
}
