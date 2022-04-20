package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private Bundle extras;
    private TextView fullNameTV;
    private TextView emailTV;
    private TextView uploadCountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        extras = getIntent().getExtras();
        fullNameTV = findViewById(R.id.fullNameTV);
        emailTV = findViewById(R.id.emailTV);
        uploadCountTV = findViewById(R.id.uploadCountTV);

        getProfile(extras.getInt("userId"));
    }

    private void getProfile(int userId) {
        Call<User> call = RetrofitClient.getInstance().getMyApi().getProfile(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    fullNameTV.setText(user.getFirstName() + " " + user.getLastName());
                    emailTV.setText(user.getEmail());
                    uploadCountTV.setText(Integer.toString(user.getUploadCount()));
                } else {
                    Log.d("onResponse","response broke");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("onFailure error", t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void startUploadActivity() {
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra("userId", extras.getInt("userId"));
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
                Toast.makeText(this, "Already in profile", Toast.LENGTH_SHORT).show();
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