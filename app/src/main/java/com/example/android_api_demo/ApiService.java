package com.example.android_api_demo;

import com.example.android_api_demo.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("food/all")
    Call<List<Food>> getAllFoods();
}
