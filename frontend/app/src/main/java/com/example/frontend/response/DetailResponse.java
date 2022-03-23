package com.example.frontend.response;

import com.google.gson.annotations.SerializedName;

public class DetailResponse {
    @SerializedName("success") private boolean success;
    @SerializedName("likes") private boolean likes;

    public boolean isSuccess() {
        return success;
    }

    public boolean hasLiked() {
        return likes;
    }
}
