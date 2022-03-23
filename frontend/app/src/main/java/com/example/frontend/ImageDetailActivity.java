package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.Body.LikeBody;
import com.example.frontend.model.Image;
import com.example.frontend.response.DetailResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageDetailActivity extends AppCompatActivity {

    private Bundle extras;
    private TextView imageDetailTitle;
    private TextView imageDetailDesc;
    private TextView imageDetailLikes;
    private TextView imageDetailTags;
    private ImageView imageDetailImage;

    private Button likeBtn;
    private Button dislikeBtn;

    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        imageDetailTitle = findViewById(R.id.imageDetailTitle);
        imageDetailDesc = findViewById(R.id.imageDetailDesc);
        imageDetailLikes = findViewById(R.id.imageDetailLikes);
        imageDetailTags = findViewById(R.id.imageDetailTags);
        imageDetailImage = findViewById(R.id.imageDetailImage);


        extras = getIntent().getExtras();
        getImageDetails();

        likeBtn = findViewById(R.id.imageDetailLike);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeImage(view);
                imageDetailLikes.setText(Integer.toString(result));
                likeBtn.setEnabled(false);
                dislikeBtn.setEnabled(true);
            }
        });

        dislikeBtn = findViewById(R.id.imageDetailDislike);
        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislikeImage(view);
                imageDetailLikes.setText(Integer.toString(result));
                likeBtn.setEnabled(true);
                dislikeBtn.setEnabled(false);
            }
        });
    }

    private void getImageDetails() {
        Call<Image> call = RetrofitClient.getInstance().getMyApi().getImageDetails(extras.getInt("imageId"));
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Image image = response.body();
                    imageDetailTitle.setText(image.getTitle());
                    imageDetailDesc.setText(image.getDescription());
                    imageDetailLikes.setText(Integer.toString(image.getLikes()));
                    imageDetailTags.setText(image.getTags().toString());
                    Picasso.get().load(image.getImgLoc()).resize(300,250).into(imageDetailImage);

                    result = image.getLikes();
                } else {
                    Log.d("onResponse","response broke");
                }
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {
                Log.e("onFailure error", t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void likeImage(View v) {
        Call<DetailResponse> call = RetrofitClient.getInstance().getMyApi().like(new LikeBody(extras.getInt("imageId"), extras.getInt("userId")));
        call.enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    DetailResponse detailResponse = response.body();
                    Log.d("onResponse", Boolean.toString(detailResponse.hasLiked()));
                    if(detailResponse.isSuccess()) {
                        int currentLikes = Integer.parseInt(imageDetailLikes.getText().toString());
                        result = currentLikes + 1;
                        Toast.makeText(getApplicationContext(), "Successfully liked image", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You've already liked this image", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("onResponse","response broke");
                }
            }

            @Override
            public void onFailure(Call<DetailResponse> call, Throwable t) {
                Log.e("onFailure error", t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dislikeImage(View v) {
        Call<DetailResponse> call = RetrofitClient.getInstance().getMyApi().dislike(new LikeBody(extras.getInt("imageId"), extras.getInt("userId")));
        call.enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    DetailResponse detailResponse = response.body();
                    Log.d("onResponse", Boolean.toString(detailResponse.hasLiked()));
                    if(detailResponse.isSuccess()) {
                        int currentLikes = Integer.parseInt(imageDetailLikes.getText().toString());
                        result = currentLikes - 1;
                        Toast.makeText(getApplicationContext(), "Successfully disliked image", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You haven't liked this image", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("onResponse", "response broke");
                }
            }

            @Override
            public void onFailure(Call<DetailResponse> call, Throwable t) {
                Log.e("onFailure error", t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });
    }
}