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
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int role;

    public User(String nameValue, String emailValue, String passwordValue) {
        this.name=nameValue;
        this.email=emailValue;
        this.password=passwordValue;
    }
}