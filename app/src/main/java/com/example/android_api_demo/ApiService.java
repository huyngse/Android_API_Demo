package com.example.android_api_demo;

import com.example.android_api_demo.models.Food;
import com.example.android_api_demo.requests.LoginRequest;
import com.example.android_api_demo.responses.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("food/all")
    Call<List<Food>> getAllFoods(@Header("Authorization") String token);

    @GET("food/{id}")
    Call<Food> getFoodById(@Header("Authorization") String token, @Path("id") int id);

    @POST("food")
    Call<Food> createFood(@Header("Authorization") String token, @Body Food food);
}
