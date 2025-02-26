package com.TheLa.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.TheLa.models.User;
import com.TheLa.services.implement.UserService;
import com.example.TheLa.databinding.ActivityForgotpasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotpasswordBinding binding;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotpasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userService = new ViewModelProvider(this).get(UserService.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        addEvents();
    }

    private void addEvents() {
        binding.btnRecovery.setOnClickListener(v -> btnClick());
    }

    private void btnClick() {
        String pass = binding.etPassword.getText().toString().trim();
        String rePass = binding.etRepeatPassword.getText().toString().trim();

        // Validate input
        if (!isValidInput(pass, rePass)) {
            return;
        }

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            user.setPassword(pass);
            if (userService.updateUser(user)) {
                Toast.makeText(this, "Password recovery successful! Redirecting to login page...", Toast.LENGTH_SHORT).show();
                binding.getRoot().postDelayed(() -> {
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }, 2000);
            }
        }
    }

    private boolean isValidInput(String password, String rePassword) {
        boolean isValid = true;
        if (password.isEmpty()) {
            binding.etPassword.setError("Please enter your password!");
            binding.etPassword.requestFocus();
            isValid = false;
        } else if (!isPasswordStrong(password)) {
            isValid = false;
        }

        if (rePassword.isEmpty()) {
            binding.etRepeatPassword.setError("Please confirm your password!");
            binding.etRepeatPassword.requestFocus();
            isValid = false;
        } else if (!password.equals(rePassword)) {
            binding.etRepeatPassword.setError("Passwords do not match!");
            binding.etRepeatPassword.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private boolean isPasswordStrong(String password) {
        // Kiểm tra độ dài mật khẩu (ít nhất 8 ký tự)
        if (password.length() < 8) {
            binding.etPassword.setError("Password must be at least 8 characters long!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một chữ cái hoa
        if (!password.matches(".*[A-Z].*")) {
            binding.etPassword.setError("Password must contain at least one uppercase letter!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một chữ cái thường
        if (!password.matches(".*[a-z].*")) {
            binding.etPassword.setError("Password must contain at least one lowercase letter!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một số
        if (!password.matches(".*[0-9].*")) {
            binding.etPassword.setError("Password must contain at least one number!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một ký tự đặc biệt
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            binding.etPassword.setError("Password must contain at least one special character!");
            binding.etPassword.requestFocus();
            return false;
        }

        return true;  // Mật khẩu đủ mạnh
    }
}