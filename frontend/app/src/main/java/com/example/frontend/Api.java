package com.example.frontend;

import com.example.frontend.model.Login;
import com.example.frontend.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    String BASE_URL = "http://192.168.1.237:3000/api/";
    @POST("authenticate/signin")
    Call<User> postLogin(@Body Login login);

    @GET("authenticate/test")
    Call<User> getUser();
}