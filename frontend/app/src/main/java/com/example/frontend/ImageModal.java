package com.example.frontend;

import java.util.List;

public class ImageModal {
    // variables for our course name,
    // description and duration.
    private String title;
    private String description;
    private String imgLoc;
    private List<String> tags;
    private int id;
    private int userId;

    // constructor class.
    public ImageModal(String title, String description, List<String> tags, int id,  int userId, String imgLoc) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.imgLoc = imgLoc;
        this.id = id;
        this.userId = userId;
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

    public String getImgLoc() {
        return imgLoc;
    }

    public void setImgLoc(String imgLoc) {
        this.imgLoc = imgLoc;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
