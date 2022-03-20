package com.example.frontend.model;

import java.util.List;

public class Upload {
    private int userId;
    private String userName;
    private String title;
    private String description;
    private List<String> tags;

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }
}
