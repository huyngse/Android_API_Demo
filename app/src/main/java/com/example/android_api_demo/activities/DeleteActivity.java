package com.example.android_api_demo.activities;

import android.os.Bundle;
import android.util.Log;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteActivity extends AppCompatActivity {
    private EditText foodIdInput;
    private Button deleteFoodButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        foodIdInput = findViewById(R.id.food_id_input);
        deleteFoodButton = findViewById(R.id.delete_food_button);
        progressBar = findViewById(R.id.progressBar);
        deleteFoodButton.setOnClickListener(v -> {
            int foodId = Integer.parseInt(foodIdInput.getText().toString());
            deleteFood(foodId);
        });
    }

    private void deleteFood(int id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getClient()
                .create(ApiService.class);
        Call<Void> call = apiService.deleteFood("Bearer " + TokenManager.getToken(getBaseContext()), id);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(DeleteActivity.this, "Delete food successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DeleteActivity.this, "Food not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}