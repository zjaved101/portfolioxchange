package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
}