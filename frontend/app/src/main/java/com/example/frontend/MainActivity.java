package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLogins();
    }

    private void getLogins() {
        view = findViewById(R.id.title);
        System.out.println("Inside getLogins");
        Call<User> call = RetrofitClient.getInstance().getMyApi().postLogin(new Login("john.doe@email.com", "1234"));
//        Call<User> call = RetrofitClient.getInstance().getMyApi().getUser();
        System.out.println("about to queue");
        view.setText("about to queue");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("INSIDE RESPONSE");
                view.setText("inside response");
                if(response.isSuccessful() && response.body() != null) {
                    User login = response.body();
                    System.out.println(login.getFirstName());
                    view.setText(String.valueOf(login.getId()));
                } else {
                    System.out.println("Response broke");
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