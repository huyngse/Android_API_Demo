package com.example.android_api_demo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetByIdActivity extends AppCompatActivity {
    private EditText foodIdInput;
    private Button fetchFoodButton;
    private ProgressBar progressBar;
    private TextView foodInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_by_id);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        foodIdInput = findViewById(R.id.food_id_et);
        fetchFoodButton = findViewById(R.id.fetch_food_btn);
        foodInfo = findViewById(R.id.food_info);
        progressBar = findViewById(R.id.progressBar);

        fetchFoodButton.setOnClickListener(v -> {
            int foodId = Integer.parseInt(foodIdInput.getText().toString());
            fetchFoodById(foodId);
        });
    }

    private void fetchFoodById(int id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getClient()
                .create(ApiService.class);
        Call<Food> call = apiService.getFoodById("Bearer " + TokenManager.getToken(getBaseContext()), id);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Food> call, @NonNull Response<Food> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Food food = response.body();
                    displayFoodInfo(food);
                } else {
                    Toast.makeText(GetByIdActivity.this, "Food not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Food> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(GetByIdActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayFoodInfo(Food food) {
        String info = "Name: " + food.getName() +
                "\nDescription: " + food.getDescription() +
                "\nPrice: $" + food.getPrice() +
                "\nImage URL: " + food.getImage();
        foodInfo.setText(info);
    }
}