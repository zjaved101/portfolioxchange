package com.example.frontend;

import com.example.frontend.model.Image;
import com.example.frontend.model.Login;
import com.example.frontend.model.User;
import com.example.frontend.response.HomePageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://192.168.1.237:3000/api/";
    @POST("authenticate/signin")
    Call<User> postLogin(@Body Login login);

    @GET("images/homepage")
    Call<HomePageResponse> getHomePage(
            @Query("userId") int userId,
            @Query("index") int index,
            @Query("length") int length
    );

}