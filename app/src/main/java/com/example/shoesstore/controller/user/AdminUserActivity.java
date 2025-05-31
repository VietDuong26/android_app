package com.example.shoesstore.controller.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesstore.R;
import com.example.shoesstore.constants.Role;
import com.example.shoesstore.controller.AdminHomeActivity;
import com.example.shoesstore.dto.UserDto;
import com.example.shoesstore.service.IUserService;
import com.example.shoesstore.service.impl.UserService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AdminUserActivity extends AppCompatActivity {
    IUserService userService;
    TableLayout tableUsers;
    MaterialButton btnAddUser;
    TextInputEditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);
        userService = new UserService(this);

        edtSearch = findViewById(R.id.edtSearch);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String nameSearch = edtSearch.getText().toString();
                if (!nameSearch.equals("") && event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    loadTable(userService.findByName(nameSearch));
                    return true;
                } else if (nameSearch.equals("")) {
                    loadTable(userService.getAll());
                    return true;
                }
                return false;
            }
        });

        btnAddUser = findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUserActivity.this, AddUserActivity.class));
            }
        });

        tableUsers = findViewById(R.id.tableUsers);
        loadTable(userService.getAll());
    }

    public void loadTable(List<UserDto> userDtos) {
        for (int i = tableUsers.getChildCount() - 1; i > 0; i--) {
            tableUsers.removeViewAt(i);
        }
        for (UserDto userDto : userDtos) {
            tableUsers = findViewById(R.id.tableUsers);
            TableRow tableRow = new TableRow(this);

            TextView index = new TextView(this);
            index.setText(String.valueOf(userDtos.indexOf(userDto) + 1));
            index.setGravity(Gravity.CENTER);

            TextView username = new TextView(this);
            username.setGravity(Gravity.CENTER);
            username.setText(userDto.getName());

            TextView email = new TextView(this);
            email.setGravity(Gravity.CENTER);
            email.setText(userDto.getEmail());

            TextView role = new TextView(this);
            role.setGravity(Gravity.CENTER);
            role.setText(Role.getRoleName(userDto.getRole()));

            float scale = getResources().getDisplayMetrics().density;
            int width = (int) (120 * scale + 0.5f);
            int height = (int) (50 * scale + 0.5f);

            TableRow.LayoutParams btnParams = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            btnParams.setMargins(8, -8, 8, 0);

            Button editBtn = new Button(this);
            editBtn.setGravity(Gravity.CENTER);
            editBtn.setText("Sửa");
            editBtn.setBackgroundResource(R.drawable.rounded_button_edit);
            editBtn.setTextColor(Color.WHITE);
            editBtn.setLayoutParams(btnParams);
            editBtn.setTextSize(14);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminUserActivity.this, EditUserActivity.class);
                    intent.putExtra("id", userDto.getId());
                    intent.putExtra("name", userDto.getName());
                    intent.putExtra("email", userDto.getEmail());
                    intent.putExtra("role", Role.getRoleName(userDto.getRole()));
                    intent.putExtra("password", userDto.getPassword());
                    startActivity(intent);
                }
            });

            Button deleteBtn = new Button(this);
            deleteBtn.setGravity(Gravity.CENTER);
            deleteBtn.setText("Xóa");
            deleteBtn.setBackgroundResource(R.drawable.rounded_button_delete);
            deleteBtn.setTextColor(Color.WHITE);
            deleteBtn.setLayoutParams(btnParams);
            deleteBtn.setTextSize(14);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!userDto.getEmail().equals("admin")) {
                        new AlertDialog.Builder(AdminUserActivity.this)
                                .setTitle("Xác nhận xóa")
                                .setMessage("Bạn có chắc chắn muốn xóa không?")
                                .setPositiveButton("Xóa", (dialog, which) -> {
                                    if (userService.delete(userDto.getId()) > 0) {
                                        Toast.makeText(AdminUserActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        loadTable(userService.getAll());
                                    } else {
                                        Toast.makeText(AdminUserActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("Hủy", null)
                                .show();
                    } else {
                        Toast.makeText(AdminUserActivity.this, "Không thể thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tableRow.addView(index);
            tableRow.addView(username);
            tableRow.addView(email);
            tableRow.addView(role);
            tableRow.addView(editBtn);
            tableRow.addView(deleteBtn);
            tableRow.setPadding(0, 8, 0, 0);
            tableUsers.addView(tableRow);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminUserActivity.this, AdminHomeActivity.class));
        super.onBackPressed();
    }
}