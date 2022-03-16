package com.example.frontend.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id") private int id;
    @SerializedName("firstName") private String firstName;
    @SerializedName("lastName") private String lastName;
    @SerializedName("token") private String token;
    @SerializedName("success") private Boolean success;

    public User(int id, String firstName, String lastName, String token, Boolean success) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = token;
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getToken() {
        return token;
    }

    public Boolean getSuccess() {
        return success;
    }
}
