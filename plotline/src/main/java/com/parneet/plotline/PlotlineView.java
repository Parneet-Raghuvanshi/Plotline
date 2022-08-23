package com.parneet.plotline;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlotlineView extends AppCompatActivity {

    private static final int GALLERY_CODE = 100,CAMERA_CODE = 200;
    private static final int CAMERA_PERMISSION_CODE = 999;
    private String TYPE = null;
    private LinearLayout processImage,retakeImage,saveImage;
    private LinearLayout saveRetakeLayout,processLayout;
    private ImageView imageViewSdk;
    private String cameraPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plotline_view);

        init();
        listeners();
        startWork();
    }

    private void startWork() {
        Intent intent = getIntent();
        TYPE = intent.getStringExtra("TYPE");
        if(TYPE.equals("GALLERY_DT") || TYPE.equals("GALLERY")) openGallery();
        if(TYPE.equals("CAMERA_DT") || TYPE.equals("CAMERA")) openCamera();
        if(TYPE.equals("URL_DT") || TYPE.equals("URL")) openUrl();
    }

    private void init() {
        processImage = findViewById(R.id.btn_process_image);
        retakeImage = findViewById(R.id.btn_retake_image);
        saveImage = findViewById(R.id.btn_save_image);
        imageViewSdk = findViewById(R.id.image_view_sdk);

        processLayout = findViewById(R.id.process_layout);
        processLayout.setVisibility(View.VISIBLE);
        saveRetakeLayout = findViewById(R.id.save_retake_layout);
        saveRetakeLayout.setVisibility(View.GONE);
    }

    private void listeners() {
        processImage.setOnClickListener(v -> {
            imageViewSdk.setImageBitmap(processImage());
            // change layout...
            processLayout.setVisibility(View.GONE);
            saveRetakeLayout.setVisibility(View.VISIBLE);
        });

        retakeImage.setOnClickListener(v -> {
            if(TYPE.equals("URL_DT")) {
                MyDB.getInstance().flush();
                finish();
            }
            if(TYPE.equals("GALLERY_DT")) openGallery();
            if(TYPE.equals("CAMERA_DT")) openCamera();
            // change layout...
            processLayout.setVisibility(View.VISIBLE);
            saveRetakeLayout.setVisibility(View.GONE);
        });

        saveImage.setOnClickListener(v -> {
            // save to firebase or just give back the data...
            PlotlineHandler plotlineHandler = MyDB.getInstance().getPlotlineHandler();
            plotlineHandler.plotlineHandler(MyDB.getInstance().getPreImage(),
                    MyDB.getInstance().getPostImage(),
                    "DETECTION");
            // exit activity...
            finish();
        });
    }

    public static Bitmap processImage() {
        try {
            // Processing bitmap after decreasing the file size, otherwise it will overflow in pixels array...
            Bitmap bitmap = MyDB.getInstance().getPreImage();
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            // Creating the Instance for Image Detection and Process the image...
            PlotlineDetector detector = new PlotlineDetector();
            detector.setSourceImage(scaled);
            detector.process();
            Bitmap finalImage = detector.getEdgesImage();

            // Saving the edged bitmap result in Application DB...
            MyDB.getInstance().setPostImage(finalImage);
            return finalImage;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void openGallery() {
        // simple open gallery intent and then get result on activityResult...
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_CODE);
    }

    private void openCamera() {
        // first of all check the permissions and then open camera for further process...
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)  {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        // Making this type of camera intent so that i can get original quality image from camera...
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File file = File.createTempFile("image_" + timeStamp,".jpg",storageDirectory);
            cameraPath = file.getAbsolutePath();

            Uri imageUri = FileProvider.getUriForFile(PlotlineView.this, "com.parneet.plotline.fileProvider", file);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_CODE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUrl() {
        // Used instead of MultiThreading, because can not parse URL on main UI thread...
        Glide.with(this)
                .asBitmap()
                .load(MyDB.getInstance().getUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageViewSdk.setImageBitmap(resource);
                        MyDB.getInstance().setPreImage(resource);

                        // If Type URL only then exit activity and return the bitmap from URL...
                        if(TYPE.equals("URL")) {
                            MyDB.getInstance().getPlotlineHandler().plotlineHandler(resource,null,"BROWSE");
                            finish();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {}
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            imageViewSdk.setImageURI(data.getData());
            BitmapDrawable drawable = (BitmapDrawable) imageViewSdk.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            MyDB.getInstance().setPreImage(bitmap);

            // If Type Gallery only then exit activity and return the bitmap from Gallery...
            if(TYPE.equals("GALLERY")) {
                MyDB.getInstance().getPlotlineHandler().plotlineHandler(bitmap,null,"BROWSE");
                finish();
            }
        } else if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(cameraPath);
            imageViewSdk.setImageBitmap(bitmap);
            MyDB.getInstance().setPreImage(bitmap);

            // If Type Camera only then exit activity and return the bitmap from Camera...
            if(TYPE.equals("CAMERA")) {
                MyDB.getInstance().getPlotlineHandler().plotlineHandler(bitmap,null,"BROWSE");
                finish();
            }
        } else finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // after permission granted continue with opening camera...
            startCamera();
        } else finish();
    }
}