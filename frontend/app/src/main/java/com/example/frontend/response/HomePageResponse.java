package com.example.frontend.response;

import com.example.frontend.model.Image;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomePageResponse {
    @SerializedName("success") private boolean success;
    @SerializedName("images") private List<Image> images;

    public boolean isSuccess() {
        return success;
    }

    public List<Image> getImages() {
        return images;
    }
}
