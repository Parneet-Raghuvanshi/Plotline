package com.parneet.plotline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;

public class Plotline {

    private final Activity activity;

    public Plotline(Activity activity) {
        this.activity = activity;
        MyDB.getInstance().setPlotlineHandler((PlotlineHandler) activity);
    }

    public Bitmap getPreImage() {
        return MyDB.getInstance().getPreImage();
    }

    public Bitmap processImage(Bitmap bitmap) {
        MyDB.getInstance().setPreImage(bitmap);
        return PlotlineView.processImage();
    }

    public Bitmap getPostImage() {
        return MyDB.getInstance().getPostImage();
    }

    public void openGalleryWithDetection() {
        startIntent("GALLERY_DT");
    }

    public void openCameraWithDetection() {
        startIntent("CAMERA_DT");
    }

    public void openUrlWithDetection(String urlString) {
        MyDB.getInstance().setUrl(urlString);
        startIntent("URL_DT");
    }

    public void openGallery() {
        startIntent("GALLERY");
    }

    public void openCamera() {
        startIntent("CAMERA");
    }

    public void openUrl(String urlString) {
        MyDB.getInstance().setUrl(urlString);
        startIntent("URL");
    }

    private void startIntent(String TYPE) {
        Intent intent = new Intent(activity,PlotlineView.class);
        intent.putExtra("TYPE",TYPE);
        activity.startActivity(intent);
    }
}
