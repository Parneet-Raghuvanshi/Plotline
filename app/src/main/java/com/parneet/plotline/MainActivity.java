package com.parneet.plotline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PlotlineHandler {

    private LinearLayout galleryBtn,cameraBtn,emptyLayout;
    private RelativeLayout urlSubmitBtn;
    private EditText editText;
    private Plotline plotline;
    private View view;
    private CustomSnacks customSnacks;

    private BucketRecyclerView recyclerView;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<ImageModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        listeners();
        rvSetup();
    }

    private void init(){
        plotline = new Plotline(MainActivity.this);
        galleryBtn = findViewById(R.id.btn_open_gallery);
        emptyLayout = findViewById(R.id.empty_layout);
        cameraBtn = findViewById(R.id.btn_open_camera);
        editText = findViewById(R.id.edit_text_main);
        urlSubmitBtn = findViewById(R.id.submit_url_button);
        recyclerView = findViewById(R.id.recyclerview);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        customSnacks = new CustomSnacks();
        view = findViewById(android.R.id.content);
    }

    private void listeners() {
        galleryBtn.setOnClickListener(v -> plotline.openGalleryWithDetection());

        cameraBtn.setOnClickListener(v -> plotline.openCameraWithDetection());

        urlSubmitBtn.setOnClickListener(v -> {
            if(editText.getText().toString().isEmpty()) return;
            plotline.openUrlWithDetection(editText.getText().toString());
            editText.setText("");
        });
    }

    private void rvSetup() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.showIfEmpty(emptyLayout);

        options = new FirebaseRecyclerOptions.Builder<ImageModel>().setQuery(databaseReference,ImageModel.class).build();
        FirebaseRecyclerAdapter<ImageModel, ImageViewHolder> adapter = new FirebaseRecyclerAdapter<ImageModel, ImageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ImageViewHolder holder, int position, @NonNull ImageModel model) {
                Glide.with(MainActivity.this).load(model.getPreImage()).into(holder.preImage);
                Glide.with(MainActivity.this).load(model.getPostImage()).into(holder.postImage);
                holder.textView.setText("IMG_" + model.getToken());
                holder.size.setText("~ "+model.getSize()+"KB");
                holder.preImage.setOnClickListener(v -> {
                    openBottomSheet("PRE_IMG_"+model.getToken(),model.getPreImage());
                });
                holder.postImage.setOnClickListener(v -> {
                    openBottomSheet("POST_IMG_"+model.getToken(),model.getPostImage());
                });
            }
            @NonNull
            @Override
            public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ImageViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.card, parent, false));
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void openBottomSheet(String name,String uri) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        //---( Bottom Sheet Variables )---//
        ImageView imageSrc = bottomSheetDialog.findViewById(R.id.edged_image);
        TextView imageName = bottomSheetDialog.findViewById(R.id.image_name);
        Button downloadBtn = bottomSheetDialog.findViewById(R.id.download_btn);
        ProgressBar progressBar = bottomSheetDialog.findViewById(R.id.progress);

        imageName.setText(name);
        Glide.with(MainActivity.this).load(uri).into(imageSrc);

        downloadBtn.setOnClickListener(v -> {
            OutputStream outputStream;
            progressBar.setVisibility(View.VISIBLE);
            downloadBtn.setVisibility(View.GONE);

            //---( Download Here )---//
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageSrc.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File dir = new File(filePath.getAbsolutePath());
            dir.mkdir();
            File file = new File(dir,name+".png");
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                outputStream.flush();
                outputStream.close();
                progressBar.setVisibility(View.GONE);
                downloadBtn.setVisibility(View.VISIBLE);
                bottomSheetDialog.dismiss();
                customSnacks.successSnack(view,"Image Saved to Downloads.");
            } catch (IOException e) {
                e.printStackTrace();
                customSnacks.failSnack(view,"Download Failed, Please try again!");
            }
        });
        bottomSheetDialog.show();
    }

    @Override
    public void plotlineHandler(Bitmap preImage,Bitmap postImage,String TYPE_CODE) {
        if(TYPE_CODE.equals("DETECTION")) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
            databaseReference.child(timeStamp).child("token").setValue(timeStamp);

            // Finding size of the Image and then Updating it also...
            Bitmap bitmap = postImage;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            long lengthBmp = imageInByte.length;
            lengthBmp = lengthBmp/1024;
            databaseReference.child(timeStamp).child("size").setValue(String.valueOf(lengthBmp));

            // Storing preImage First...
            Uri preImageUri = getImageUri(this,preImage,"preimage_"+timeStamp);
            StorageReference preImageName = storageReference.child(timeStamp).child(preImageUri.getLastPathSegment());
            preImageName.putFile(preImageUri).addOnSuccessListener(taskSnapshot -> preImageName.getDownloadUrl().addOnSuccessListener(uri -> {
                databaseReference.child(timeStamp).child("preImage").setValue(String.valueOf(uri));
            }));

            // Storing postImage Then...
            Uri postImageUri = getImageUri(this,postImage,"postimage_"+timeStamp);
            StorageReference postImageName = storageReference.child(timeStamp).child(postImageUri.getLastPathSegment());
            postImageName.putFile(postImageUri).addOnSuccessListener(taskSnapshot -> postImageName.getDownloadUrl().addOnSuccessListener(uri -> {
                databaseReference.child(timeStamp).child("postImage").setValue(String.valueOf(uri));
            }));

            customSnacks.successSnack(view,"Image Saved to Firebase Database.");
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage,String imageName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imageName, null);
        return Uri.parse(path);
    }
}
