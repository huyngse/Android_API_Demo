package com.example.android_api_demo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_api_demo.ApiService;
import com.example.android_api_demo.R;
import com.example.android_api_demo.RetrofitClient;
import com.example.android_api_demo.models.Food;
import com.example.android_api_demo.utils.TokenManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity {
    private EditText imageInput;
    private EditText nameInput;
    private EditText descriptionInput;
    private EditText priceInput;
    private Button createFoodButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageInput = findViewById(R.id.create_image_input);
        nameInput = findViewById(R.id.create_name_input);
        descriptionInput = findViewById(R.id.create_description_input);
        priceInput = findViewById(R.id.create_price_input);
        createFoodButton = findViewById(R.id.create_food_button);
        progressBar = findViewById(R.id.progressBar);
        createFoodButton.setOnClickListener(v -> createFood());
    }

    private void createFood() {
        progressBar.setVisibility(View.VISIBLE);
        String image = imageInput.getText().toString();
        String name = nameInput.getText().toString();
        String description = descriptionInput.getText().toString();
        double price = Double.parseDouble(priceInput.getText().toString());

        Food newFood = new Food(image, name, description, price);
        ApiService apiService = RetrofitClient.getClient()
                .create(ApiService.class);
        Call<Food> call = apiService.createFood("Bearer " + TokenManager.getToken(getBaseContext()), newFood);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Food> call, @NonNull Response<Food> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    clearInputs();
                    Toast.makeText(CreateActivity.this, "Create food successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String message = null;
                        if (response.errorBody() != null) {
                            message = response.errorBody().string();
                        }
                        Toast.makeText(CreateActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(CreateActivity.this, "Failed to create foods", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Food> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputs() {
        imageInput.setText("");
        nameInput.setText("");
        descriptionInput.setText("");
        priceInput.setText("");
    }
}