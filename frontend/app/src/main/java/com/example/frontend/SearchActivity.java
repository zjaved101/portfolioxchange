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
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private CheckBox nature;
    private CheckBox automotive;
    private CheckBox happy;
    private CheckBox sad;
    private CheckBox angry;
    private CheckBox food;

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        nature = findViewById(R.id.natureCB);
        automotive = findViewById(R.id.automotiveCB);
        happy = findViewById(R.id.happyCB);
        sad = findViewById(R.id.sadCB);
        angry = findViewById(R.id.angryCB);
        food = findViewById(R.id.foodCB);

        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchResultActivity();
            }
        });

        extras = getIntent().getExtras();
    }

    private ArrayList<String> getTags() {
        ArrayList<String> tags = new ArrayList<>();
        if(nature.isChecked())
            tags.add("nature");
        if(automotive.isChecked())
            tags.add("automotive");
        if(happy.isChecked())
            tags.add("happy");
        if(sad.isChecked())
            tags.add("sad");
        if(angry.isChecked())
            tags.add("angry");
        if(food.isChecked())
            tags.add("food");

        return tags;
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

    private void startUploadActivity() {
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra("userId",extras.getInt("userId"));
        intent.putExtra("firstName", extras.getString("firstName"));
        intent.putExtra("lastName", extras.getString("lastName"));
        intent.putExtra("token", extras.getString("token"));
        startActivity(intent);
    }

    private void startSearchResultActivity() {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("userId", extras.getInt("userId"));
        intent.putExtra("firstName", extras.getString("firstName"));
        intent.putExtra("lastName", extras.getString("lastName"));
        intent.putExtra("token", extras.getString("token"));
        intent.putStringArrayListExtra("tags", getTags());
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
                Toast.makeText(getApplicationContext(), "Already on search page", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_upload:
                startUploadActivity();
                return true;

            case R.id.action_profile:
                startProfileActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}