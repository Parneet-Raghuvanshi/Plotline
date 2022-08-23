package com.parneet.plotline;

import android.graphics.Bitmap;

public class MyDB {

    private static MyDB instance = null;
    private Bitmap preImage,postImage;
    private PlotlineHandler plotlineHandler;
    private String url;

    private MyDB() {
        this.preImage = null;
        this.postImage = null;
        this.plotlineHandler = null;
        this.url = null;
    }

    public PlotlineHandler getPlotlineHandler() {
        return plotlineHandler;
    }

    public void setPlotlineHandler(PlotlineHandler plotlineHandler) {
        this.plotlineHandler = plotlineHandler;
    }

    public void flush() {
        this.preImage = null;
        this.postImage = null;
        this.url = null;
    }

    public static MyDB getInstance() {
        if(instance == null) instance = new MyDB();
        return instance;
    }

    public Bitmap getPreImage() {
        return preImage;
    }

    public void setPreImage(Bitmap preImage) {
        this.preImage = preImage;
    }

    public Bitmap getPostImage() {
        return postImage;
    }

    public void setPostImage(Bitmap postImage) {
        this.postImage = postImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
