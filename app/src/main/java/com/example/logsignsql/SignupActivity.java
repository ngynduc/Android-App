package com.example.logsignsql;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.logsignsql.databinding.ActivitySignupBinding;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();

                if(email.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(SignupActivity.this, "Tất cả các trường đều bắt buộc", Toast.LENGTH_SHORT).show();
                } else {
                    if(password.equals(confirmPassword)){
                        Boolean checkUserEmail = databaseHelper.checkEmail(email);

                        if(checkUserEmail == false){
                            // Băm mật khẩu trước khi lưu
                            String hashedPassword = hashPassword(password);
                            Boolean insert = databaseHelper.insertData(email, hashedPassword);

                            if(insert == true){
                                Toast.makeText(SignupActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "Người dùng đã tồn tại! Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Phương thức băm mật khẩu
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
