package com.example.shoesstore.controller.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.R;
import com.example.shoesstore.constants.Role;
import com.example.shoesstore.model.UserModel;
import com.example.shoesstore.service.IUserService;
import com.example.shoesstore.service.impl.UserService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.stream.Collectors;

public class EditUserActivity extends AppCompatActivity {
    IUserService userService;
    AutoCompleteTextView actvRole;
    TextInputEditText edtUserName, edtEmail, edtPassword;
    MaterialButton btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        userService = new UserService(this);

        int id = getIntent().getIntExtra("id", 0);

        btnUpdate = findViewById(R.id.btnUpdate);

        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        edtUserName.setText(getIntent().getStringExtra("name"));
        edtEmail.setText(getIntent().getStringExtra("email"));
        edtPassword.setText(getIntent().getStringExtra("password"));

        actvRole = findViewById(R.id.actvRole);
        actvRole.setText(getIntent().getStringExtra("role"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                Role.getAllRole().stream().map(x -> Role.getRoleName(x.getValue())).collect(Collectors.toList())
        );
        actvRole.setAdapter(adapter);
        actvRole.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                actvRole.showDropDown();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtUserName.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String role = actvRole.getText().toString();
                if (!name.equals("") && !email.equals("") && !password.equals("") && !role.equals("")) {
                    if (email.contains("@gmail.com")) {
                        if (userService.update(new UserModel(name, email, password, role), id) > 0) {
                            Toast.makeText(EditUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditUserActivity.this, AdminUserActivity.class));
                        } else {
                            Toast.makeText(EditUserActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditUserActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditUserActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditUserActivity.this, AdminUserActivity.class));
        super.onBackPressed();
    }
}