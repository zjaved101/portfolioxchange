package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.frontend.model.Image;
import com.example.frontend.response.HomePageResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_home);

        extras = getIntent().getExtras();

        imageRV = findViewById(R.id.images);
        loadingPB = findViewById(R.id.loading);
        nestedSV = findViewById(R.id.nestedSV);

        imageModalArrayList = new ArrayList<>();

        imageRVAdapter = new ImageRVAdapter(HomeActivity.this, imageModalArrayList);
        imageRV.setAdapter(imageRVAdapter);

        getData(extras.getInt("userId"), 1, 100);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        imageRV.setLayoutManager(manager);

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    count++;
                    loadingPB.setVisibility(View.VISIBLE);
                    if (count < 5) {
                        getData(extras.getInt("userId"), 1, 100);
                    }
                }
            }
        });
    }

    private void getData(int userId, int index, int length) {
        Log.d("getData", "INSIDE GETDATA");
        Call<HomePageResponse> call = RetrofitClient.getInstance().getMyApi().getHomePage(userId, index, length);
        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, Response<HomePageResponse> response) {
                imageRV.setVisibility(View.VISIBLE);
                Log.d("getData", "INSIDE RESPONSE");
                Log.d("getData", response.toString());
                if(response.isSuccessful() && response.body() != null) {
                    HomePageResponse homepage = response.body();
                    for (Image elem : homepage.getImages()) {
                        Log.d("getData", elem.getDescription());

                        imageModalArrayList.add(new ImageModal(elem.getTitle(), elem.getDescription(), elem.getImgLoc()));
                        imageRVAdapter = new ImageRVAdapter(HomeActivity.this, imageModalArrayList);
                        imageRV.setAdapter(imageRVAdapter);
                    }
                    loadingPB.setVisibility(View.INVISIBLE);
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
}