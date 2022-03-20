package com.example.frontend;

import com.example.frontend.model.Image;
import com.example.frontend.model.Login;
import com.example.frontend.model.Upload;
import com.example.frontend.model.User;
import com.example.frontend.response.GeneralResponse;
import com.example.frontend.response.HomePageResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Multipart
    @POST("images/upload")
    Call<GeneralResponse> uploadImage(
            @Part("userId") Integer userId,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("tags") ArrayList<String> tags,
            @Part MultipartBody.Part image
    );
}