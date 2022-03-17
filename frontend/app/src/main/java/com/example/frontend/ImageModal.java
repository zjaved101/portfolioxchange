package com.example.frontend;

public class ImageModal {
    // variables for our course name,
    // description and duration.
    private String title;
    private String description;
    private String imgLoc;

    // constructor class.
    public ImageModal(String title, String description, String imgLoc) {
        this.title = title;
        this.description = description;
        this.imgLoc = imgLoc;
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
}
