package com.example.android_api_demo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {
    private Spinner foodSpinner;
    private EditText imageInput;
    private EditText nameInput;
    private EditText descriptionInput;
    private EditText priceInput;
    private Button updateFoodButton;
    private List<Food> foodList = new ArrayList<>();
    private ApiService apiService;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        foodSpinner = findViewById(R.id.food_spinner);
        imageInput = findViewById(R.id.update_image_input);
        nameInput = findViewById(R.id.update_name_input);
        descriptionInput = findViewById(R.id.update_description_input);
        priceInput = findViewById(R.id.update_price_input);
        updateFoodButton = findViewById(R.id.update_food_button);
        progressBar = findViewById(R.id.progressBar);

        apiService = RetrofitClient.getClient()
                .create(ApiService.class);

        loadFoodItems();

        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Food selectedFood = foodList.get(position);
                populateFoodDetails(selectedFood);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                clearInputs();
            }
        });

        updateFoodButton.setOnClickListener(v -> updateFood());
    }

    private void loadFoodItems() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<Food>> call = apiService.getAllFoods("Bearer " + TokenManager.getToken(getBaseContext()));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    foodList = response.body();
                    ArrayAdapter<Food> adapter = null;
                    if (foodList != null) {
                        adapter = new ArrayAdapter<>(UpdateActivity.this,
                                android.R.layout.simple_spinner_item, foodList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    }
                    foodSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(UpdateActivity.this, "Failed to load food items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Food>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFoodDetails(Food food) {
        imageInput.setText(food.getImage());
        nameInput.setText(food.getName());
        descriptionInput.setText(food.getDescription());
        priceInput.setText(String.valueOf(food.getPrice()));
    }

    private void updateFood() {
        progressBar.setVisibility(View.VISIBLE);
        int position = foodSpinner.getSelectedItemPosition();
        Food selectedFood = foodList.get(position);
        int id = selectedFood.getId();
        String image = imageInput.getText().toString();
        String name = nameInput.getText().toString();
        String description = descriptionInput.getText().toString();
        double price = Double.parseDouble(priceInput.getText().toString());

        Food updatedFood = new Food(image, name, description, price);

        Call<Food> call = apiService.updateFood("Bearer " + TokenManager.getToken(getBaseContext()), id, updatedFood);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Food> call, @NonNull Response<Food> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    clearInputs();
                    Toast.makeText(UpdateActivity.this, "Update food successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateActivity.this, "Failed to update food", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Food> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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