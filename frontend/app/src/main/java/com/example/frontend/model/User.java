package com.example.frontend.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("id") private int id;
    @SerializedName("firstName") private String firstName;
    @SerializedName("lastName") private String lastName;
    @SerializedName("email") private String email;
    @SerializedName("portfolio") private List<Integer> portfolio;
    @SerializedName("uploadCount") private int uploadCount;
    @SerializedName("likeCount") private int likeCount;
    @SerializedName("token") private String token;
    @SerializedName("success") private Boolean success;

    public User(int id, String firstName, String lastName, String email, List<Integer> portfolio, int uploadCount, int likeCount, String token, Boolean success) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.portfolio = portfolio;
        this.uploadCount = uploadCount;
        this.likeCount = likeCount;
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

    public String getEmail() {
        return email;
    }

    public List<Integer> getPortfolio() {
        return portfolio;
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPortfolio(List<Integer> portfolio) {
        this.portfolio = portfolio;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
