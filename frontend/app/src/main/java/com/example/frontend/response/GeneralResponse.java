package com.example.frontend.response;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse {
    @SerializedName("success") private boolean success;

    public boolean isSuccess() {
        return success;
    }
}
