package com.example.shoesstore.controller.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoesstore.R;
import com.example.shoesstore.constants.Role;
import com.example.shoesstore.model.UserModel;
import com.example.shoesstore.service.IUserService;
import com.example.shoesstore.service.impl.UserService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.stream.Collectors;

public class AddUserActivity extends AppCompatActivity {
    IUserService userService;
    AutoCompleteTextView actvRole;
    TextInputEditText edtUserName,edtEmail,edtPassword;
    MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        userService = new UserService(this);

        edtUserName=findViewById(R.id.edtUserName);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
        btnSave=findViewById(R.id.btnSave);

        actvRole = findViewById(R.id.actvRole);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                Role.getAllRole().stream().map(x->Role.getRoleName(x.getValue())).collect(Collectors.toList())
        );
        actvRole.setAdapter(adapter);
        actvRole.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                actvRole.showDropDown();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edtUserName.getText().toString();
                String email=edtEmail.getText().toString();
                String password=edtPassword.getText().toString();
                String role=actvRole.getText().toString();
                if(!name.equals("")&&!email.equals("")&&!password.equals("")&&!role.equals("")) {
                    if(email.contains("@gmail.com")){
                        if(userService.add(new UserModel(name,email,password,role))>0){
                            Toast.makeText(AddUserActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddUserActivity.this, AdminUserActivity.class));
                        }else{
                            Toast.makeText(AddUserActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(AddUserActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddUserActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}