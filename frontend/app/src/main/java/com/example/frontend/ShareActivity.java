package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.Body.ShareBody;
import com.example.frontend.model.User;
import com.example.frontend.response.GeneralResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareActivity extends AppCompatActivity {

    private EditText emailET;
    private Button sendBtn;
    private Bundle extras;
    private TextView userIdTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        userIdTV = findViewById(R.id.shareUserIdTV);
        emailET = findViewById(R.id.shareEmailET);
        sendBtn = findViewById(R.id.shareSendBtn);
        extras = getIntent().getExtras();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });
    }

    private void getUser() {
        Call<User> call = RetrofitClient.getInstance().getMyApi().getUser(emailET.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Log.e("onResponse", "GOT USER");
                    Log.e("onResponse", user.getEmail());
                    Log.e("onResponse", Integer.toString(user.getId()));
                    userIdTV.setText(Integer.toString(user.getId()));
                } else {
                    Log.d("onResponse", "response broke");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("onFailure error", t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void shareImage() {
        Call<User> userCall = RetrofitClient.getInstance().getMyApi().getUser(emailET.getText().toString());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> userCall, Response<User> userResponse) {
                if(userResponse.isSuccessful() && userResponse.body() != null) {
                    User user = userResponse.body();
                    Log.e("onResponse", "GOT USER");
                    Log.e("onResponse", user.getEmail());
                    Log.e("onResponse", Integer.toString(user.getId()));
                    Log.e("shareImage", Integer.toString(extras.getInt("authorId")));
                    Log.e("shareImage", Integer.toString(extras.getInt("imageId")));
                    Log.e("shareImage", userIdTV.getText().toString());
                    userIdTV.setText(Integer.toString(user.getId()));

                    Call<GeneralResponse> call = RetrofitClient.getInstance().getMyApi().shareImage(new ShareBody(Integer.parseInt(userIdTV.getText().toString()), extras.getInt("authorId"), extras.getInt("imageId")));
                    call.enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            if(response.isSuccessful() && response.body() != null) {
                                GeneralResponse generalResponse = response.body();
                                Log.d("onResponse", Boolean.toString(generalResponse.isSuccess()));
                                if(generalResponse.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), "You have shared the image", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "You already have shared the image", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.d("onResponse", "response broke");
                            }
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            Log.e("onFailure error", t.getMessage());
                            Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Log.d("onResponse", "response broke");
                }
            }

            @Override
            public void onFailure(Call<User> userCall, Throwable t) {
                Log.e("onFailure error", t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });
    }
}