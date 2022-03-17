package com.example.frontend.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Image {
    @SerializedName("id") private int id;
    @SerializedName("title") private String title;
    @SerializedName("description") private String description;
    @SerializedName("imgType") private String imgType;
    @SerializedName("tags") private List<String> tags;
    @SerializedName("imgLoc") private String imgLoc;
    @SerializedName("likes") private int likes;
    @SerializedName("likeList") private List<Integer> likeList;
    @SerializedName("UserId") private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getImgLoc() {
        return imgLoc;
    }

    public void setImgLoc(String imgLoc) {
        this.imgLoc = imgLoc;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Integer> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Integer> likeList) {
        this.likeList = likeList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
