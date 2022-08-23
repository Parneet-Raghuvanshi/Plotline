package com.parneet.plotline;

public class ImageModel {
    private String token;
    private String preImage;
    private String postImage;
    private String size;

    public ImageModel() {
    }

    public ImageModel(String token, String preImage, String postImage, String size) {
        this.token = token;
        this.preImage = preImage;
        this.postImage = postImage;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPreImage() {
        return preImage;
    }

    public void setPreImage(String preImage) {
        this.preImage = preImage;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
}
