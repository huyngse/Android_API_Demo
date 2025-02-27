package com.example.android_api_demo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_all);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        fetchFoods();
    }

    private void fetchFoods() {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getClient()
                .create(ApiService.class);
        Call<List<Food>> call = apiService.getAllFoods("Bearer " + TokenManager.getToken(getBaseContext()));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Food> foods = response.body();
                    displayProducts(foods);
                } else {
                    try {
                        String message = null;
                        if (response.errorBody() != null) {
                            message = response.errorBody().string();
                        }
                        Toast.makeText(GetAllActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(GetAllActivity.this, "Failed to fetch foods", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Food>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(GetAllActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProducts(List<Food> foods) {
        String[] productNames = new String[foods.size()];
        for (int i = 0; i < foods.size(); i++) {
            productNames[i] = foods.get(i).getName() + " - $" + foods.get(i).getPrice();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productNames);
        listView.setAdapter(adapter);
    }
}