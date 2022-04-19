package com.example.frontend.Body;

public class ShareBody {

    private int imageId;
    private int userId;
    private int authorId;

    public ShareBody(int userId, int authorId, int imageId) {
        this.imageId = imageId;
        this.authorId = authorId;
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

    public int getAuthorId() {
        return authorId;
    }
}
