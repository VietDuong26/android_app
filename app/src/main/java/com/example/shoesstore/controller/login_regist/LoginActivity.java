package com.example.shoesstore.controller.login_regist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.shoesstore.R;
import com.example.shoesstore.controller.AdminHomeActivity;
import com.example.shoesstore.controller.HomeActivity;
import com.example.shoesstore.dto.UserDto;
import com.example.shoesstore.service.impl.UserService;
import com.example.shoesstore.util.CheckLogin;


public class LoginActivity extends AppCompatActivity {
    private UserService userService;
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userService = new UserService(this);
        edtUsername = findViewById(R.id.emailInput);
        edtPassword = findViewById(R.id.passwordInput);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (!username.isEmpty() && !password.isEmpty()) {
                    UserDto user = userService.login(username, password);
                    if (user != null) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        CheckLogin.setUserId(LoginActivity.this, user.getId());
                        if (user.getRole() == 0) {
                            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra lại mật khẩu hoặc tên đăng nhập!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRegister = findViewById(R.id.tvRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}