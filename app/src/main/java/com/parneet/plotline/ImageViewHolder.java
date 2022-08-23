package com.parneet.plotline;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewHolder extends RecyclerView.ViewHolder {

    TextView textView;
    ImageView preImage;
    ImageView postImage;
    TextView size;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.text_name);
        preImage = itemView.findViewById(R.id.pre_image);
        postImage = itemView.findViewById(R.id.post_image);
        size = itemView.findViewById(R.id.size_text);
    }
}
