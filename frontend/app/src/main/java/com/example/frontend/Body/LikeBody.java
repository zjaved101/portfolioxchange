package com.example.frontend.Body;

public class LikeBody {
    private int imageId;
    private int userId;

    public LikeBody(int imageid, int userId) {
        this.imageId = imageid;
        this.userId = userId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
