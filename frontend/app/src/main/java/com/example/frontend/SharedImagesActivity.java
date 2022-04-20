package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.frontend.model.Image;
import com.example.frontend.response.HomePageResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedImagesActivity extends AppCompatActivity {

    int count = 0;
    private ArrayList<ImageModal> imageModalArrayList;
    private RecyclerView imageRV;
    private ImageRVAdapter imageRVAdapter;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_images);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        extras = getIntent().getExtras();
        Log.d("Home", Integer.toString(extras.getInt("userId")));

        imageRV = findViewById(R.id.images);
        loadingPB = findViewById(R.id.loading);
        nestedSV = findViewById(R.id.nestedSV);

        imageModalArrayList = new ArrayList<>();

        imageRVAdapter = new ImageRVAdapter(SharedImagesActivity.this, imageModalArrayList, extras);
        imageRV.setAdapter(imageRVAdapter);

        getData(extras.getInt("userId"), 0, 100);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        imageRV.setLayoutManager(manager);

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    loadingPB.setVisibility(View.VISIBLE);
                    getData(extras.getInt("userId"), 0, 100);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getData(extras.getInt("userId"), 0, 100);
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

    private void getData(int userId, int index, int length) {
        Call<HomePageResponse> call = RetrofitClient.getInstance().getMyApi().getShared(extras.getInt("userId"), index, length);
        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, Response<HomePageResponse> response) {
                imageRV.setVisibility(View.VISIBLE);
                Log.d("getData", "INSIDE RESPONSE");
                Log.d("getData", response.toString());
                if(response.isSuccessful() && response.body() != null) {
                    List<Image> images = response.body().getImages();
                    for(int i = count; i < images.size(); i++) {
                        Image elem = images.get(i);
                        Log.d("getData", elem.getDescription());
                        imageModalArrayList.add(new ImageModal(elem.getTitle(), elem.getDescription(), elem.getTags(), elem.getId(), elem.getUserId(), elem.getImgLoc()));
                        imageRVAdapter = new ImageRVAdapter(SharedImagesActivity.this, imageModalArrayList, extras);
                        imageRV.setAdapter(imageRVAdapter);
                    }
                    loadingPB.setVisibility(View.INVISIBLE);
                    count = images.size();
                } else {
                    Log.d("onResponse","response broke");
                }
            }

            @Override
            public void onFailure(Call<HomePageResponse> call, Throwable t) {
                Log.e("onFailure error", t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }

        });
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
                Toast.makeText(getApplicationContext(), "Already on home page", Toast.LENGTH_LONG).show();
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
//                startSharedImagesActivity();
                Toast.makeText(this, "Already in Shared Images", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}