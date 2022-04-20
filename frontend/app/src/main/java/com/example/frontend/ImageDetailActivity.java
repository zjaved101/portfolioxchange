package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
//    private Button shareBtn;

    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        imageDetailTitle = findViewById(R.id.imageDetailTitle);
        imageDetailDesc = findViewById(R.id.imageDetailDesc);
        imageDetailLikes = findViewById(R.id.imageDetailLikes);
        imageDetailTags = findViewById(R.id.imageDetailTags);
        imageDetailImage = findViewById(R.id.imageDetailImage);


        extras = getIntent().getExtras();
        Log.d("ImageDetail", Integer.toString(extras.getInt("userId")));
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

//        shareBtn = findViewById(R.id.imageDetailShare);
//        shareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startShareActivity();
//            }
//        });
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

    private void startShareActivity() {
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra("authorId", extras.getInt("authorId"));
        intent.putExtra("imageId", extras.getInt("imageId"));
        startActivity(intent);
    }

    private void startUploadActivity() {
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra("userId",extras.getInt("userId"));
        intent.putExtra("firstName", extras.getString("firstName"));
        intent.putExtra("lastName", extras.getString("lastName"));
        intent.putExtra("token", extras.getString("token"));
        startActivity(intent);
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId",extras.getInt("userId"));
        intent.putExtra("firstName", extras.getString("firstName"));
        intent.putExtra("lastName", extras.getString("lastName"));
        intent.putExtra("token", extras.getString("token"));
        startActivity(intent);
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        Log.d("Login", Integer.toString(extras.getInt("userId")));
        intent.putExtra("userId", extras.getInt("userId"));
        intent.putExtra("firstName", extras.getString("firstName"));
        intent.putExtra("lastName", extras.getString("lastName"));
        intent.putExtra("token", extras.getString("token"));
        startActivity(intent);
    }

    private void startSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("userId",extras.getInt("userId"));
        intent.putExtra("firstName", extras.getString("firstName"));
        intent.putExtra("lastName", extras.getString("lastName"));
        intent.putExtra("token", extras.getString("token"));
        startActivity(intent);
    }

    private void startUploadedImagesActivity() {
        Intent intent = new Intent(this, UploadedImagesActivity.class);
        intent.putExtra("userId",extras.getInt("userId"));
        startActivity(intent);
    }

    private void startSharedImagesActivity() {
        Intent intent = new Intent(this, SharedImagesActivity.class);
        intent.putExtra("userId",extras.getInt("userId"));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                startHomeActivity();
                return true;

            case R.id.action_search:
                startSearchActivity();
                return true;

            case R.id.action_upload:
                startUploadActivity();
                return true;

            case R.id.action_profile:
                startProfileActivity();
                return true;

            case R.id.action_uploaded:
                startUploadedImagesActivity();
                return true;

            case R.id.action_shared:
                startSharedImagesActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}