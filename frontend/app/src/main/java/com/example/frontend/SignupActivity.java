package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frontend.model.Login;
import com.example.frontend.model.Signup;
import com.example.frontend.model.User;
import com.example.frontend.response.GeneralResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signup = findViewById(R.id.signupBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signup(v);
            }
        });
    }

    private void signup(View v) {
        EditText firstName = findViewById(R.id.firstNameET);
        EditText lastName = findViewById(R.id.lastNameET);
        EditText email = findViewById(R.id.emailET);
        EditText password = findViewById(R.id.passwordET);

        Call<User> call = RetrofitClient.getInstance().getMyApi().signup(new Signup(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), password.getText().toString()));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null) {
                    User login = response.body();
                    if(login.getSuccess()) {
                        startHomeActivity(v, login);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Account creation failed", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("onFailure error", "Response broke");
                    Toast.makeText(getApplicationContext(), "Login failed, please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("onFailure error", t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startHomeActivity(View v, User user) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("userId",user.getId());
        intent.putExtra("firstName", user.getFirstName());
        intent.putExtra("lastName", user.getLastName());
        intent.putExtra("token", user.getToken());
        startActivity(intent);
    }
}