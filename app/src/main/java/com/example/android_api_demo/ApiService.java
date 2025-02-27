package com.example.android_api_demo;

import com.example.android_api_demo.models.Food;
import com.example.android_api_demo.requests.LoginRequest;
import com.example.android_api_demo.response.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
    @GET("food/all")
    Call<List<Food>> getAllFoods(@Header("Authorization") String token);
}
