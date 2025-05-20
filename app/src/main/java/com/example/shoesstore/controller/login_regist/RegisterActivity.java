package com.example.shoesstore.controller.login_regist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.shoesstore.R;
import com.example.shoesstore.model.UserModel;
import com.example.shoesstore.service.IUserService;
import com.example.shoesstore.service.impl.UserService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText name, email, password, confirmPassword;
    MaterialButton btnRegister;
    TextView btnLogin;
    private IUserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        userService = new UserService(this);

        btnLogin = findViewById(R.id.tvLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameValue = name.getText().toString().trim();
                String emailValue = email.getText().toString().trim();
                String passwordValue = password.getText().toString().trim();
                String confirmPasswordValue = confirmPassword.getText().toString().trim();
                if (!nameValue.isEmpty()
                        && !emailValue.isEmpty()
                        && !passwordValue.isEmpty()
                        && !confirmPasswordValue.isEmpty()
                ) {
                    if (passwordValue.equals(confirmPasswordValue)) {
                        if (emailValue.contains("@gmail.com")) {
                            if (userService.add(new UserModel(nameValue, emailValue, passwordValue,null)) != 0) {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Email không hợp lệ!!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}