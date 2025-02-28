package com.example.android_api_demo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_api_demo.R;
import com.example.android_api_demo.utils.TokenManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button getAllButton = findViewById(R.id.menu_get_all_btn);
        Button loginButton = findViewById(R.id.menu_login_btn);
        Button clearTokenButton = findViewById(R.id.menu_clear_token_btn);
        Button getByIdButton = findViewById(R.id.menu_get_by_id_btn);
        Button createButton = findViewById(R.id.menu_create_new_btn);
        Button deleteButton = findViewById(R.id.menu_delete_btn);
        getAllButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GetAllActivity.class);
            startActivity(intent);
        });
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        getByIdButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GetByIdActivity.class);
            startActivity(intent);
        });
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateActivity.class);
            startActivity(intent);
        });
        deleteButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeleteActivity.class);
            startActivity(intent);
        });
        clearTokenButton.setOnClickListener(v -> {
            TokenManager.clearToken(getBaseContext());
            Toast.makeText(this, "Clear token successfully", Toast.LENGTH_SHORT).show();
        });
    }
}