package com.example.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageUploadedRVAdapter extends RecyclerView.Adapter<ImageUploadedRVAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ImageModal> imageModalArrayList;
    private Bundle extras;

    public ImageUploadedRVAdapter(Context context, ArrayList<ImageModal> imageModalArrayList, Bundle extras) {
        this.context = context;
        this.imageModalArrayList = imageModalArrayList;
        Picasso.get().setLoggingEnabled(true);

        this.extras = extras;
    }

    @NonNull
    @Override
    public ImageUploadedRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageUploadedRVAdapter.ViewHolder holder, int position) {
        ImageModal images = imageModalArrayList.get(position);
        holder.imageTitleTV.setText(images.getTitle());
        holder.imageDescTV.setText(images.getDescription());
        holder.imageTagsTV.setText(images.getTags().toString());
        holder.imageIdTV.setText(Integer.toString(images.getId()));
        holder.userIdTV.setText(Integer.toString(images.getUserId()));
        Picasso.get().load(images.getImgLoc()).resize(100,100).into(holder.imageIV);

    }

    @Override
    public int getItemCount() {
        return imageModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageIV;
        private final TextView imageTitleTV;
        private final TextView imageDescTV;
        private final TextView imageTagsTV;
        private final TextView imageIdTV;
        private final TextView userIdTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.imageIV);
            imageTitleTV = itemView.findViewById(R.id.imageTitle);
            imageDescTV = itemView.findViewById(R.id.imageDesc);
            imageTagsTV = itemView.findViewById(R.id.imageTags);
            imageIdTV = itemView.findViewById(R.id.imageId);
            userIdTV = itemView.findViewById(R.id.userId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startImageShareDetailActivity(view);
                }
            });
        }

        private void startImageShareDetailActivity(View v) {
            Intent intent = new Intent(context, ImageShareDetailActivity.class);
            intent.putExtra("imageId", Integer.parseInt(imageIdTV.getText().toString()));
            intent.putExtra("userId", extras.getInt("userId"));
            intent.putExtra("authorId", Integer.parseInt(userIdTV.getText().toString()));
            context.startActivity(intent);
        }
    }
}
