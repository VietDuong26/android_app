package com.example.shoesstore.constants;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ROLE_ADMIN(0),
    ROLE_USER(1);
    
    private int value;

    Role(int value) {
        this.value = value;
    }
    
    public int getValue(){
        return this.value;
    }
    
    public static String getRoleName(int value){
        if(value == ROLE_ADMIN.getValue()){
            return "ROLE_ADMIN";
        }else{
            return "ROLE_USER";
        }
    }
    public static List<Role> getAllRole(){
        return Arrays.asList(Role.values());
    }
}
