package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frontend.response.GeneralResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 9544;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ImageView image;
    private String part_image;
    private EditText titleET;
    private EditText descET;
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
        setContentView(R.layout.activity_upload);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        image = findViewById(R.id.img);
        titleET = findViewById(R.id.titleET);
        descET = findViewById(R.id.descET);
        nature = findViewById(R.id.natureCB);
        automotive = findViewById(R.id.automotiveCB);
        happy = findViewById(R.id.happyCB);
        sad = findViewById(R.id.sadCB);
        angry = findViewById(R.id.angryCB);
        food = findViewById(R.id.foodCB);
        Button uploadBtn = findViewById(R.id.uploadBtn);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadImage(v);
            }
        });

        extras = getIntent().getExtras();
    }

    public void pick(View view) {
        verifyStoragePermissions(UploadActivity.this);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pick Image"), PICK_IMAGE_REQUEST);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData(); // Get the image file URI
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(bitmap);  // Set the ImageView with the bitmap of the image

                Cursor returnCursor = this.getContentResolver().query(selectedImage, new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE}, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();

                String name = (returnCursor.getString(nameIndex));
                Log.d("UploadActivity/onActivityResult", name);
                String size = (Long.toString(returnCursor.getLong(sizeIndex)));

                File output = new File(this.getFilesDir() + "/" + name);
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(selectedImage);
                    FileOutputStream outputStream = new FileOutputStream(output);
                    int read = 0;
                    int bufferSize = 1024;
                    final byte[] buffers = new byte[bufferSize];
                    while ((read = inputStream.read(buffers)) != -1) {
                        outputStream.write(buffers, 0, read);
                    }
                    inputStream.close();
                    outputStream.close();
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                part_image = output.getPath();
                Log.d("UploadActivity/onActivityResult", part_image);

                File test = new File(part_image);
                Log.d("UploadActivity/onActivityResult", test.getAbsolutePath());
                if(test.exists())
                    Log.d("UploadActivity/onActivityResult", "FILE EXISTS");
                else
                    Log.d("UploadActivity/onActivityResult", "FILE DOES NOT EXIST");
            }
        }
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

    private void uploadImage(View v) {
        File imageFile = new File(part_image);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", imageFile.getName(), reqFile);

        Log.d("UploadActivity/onResponse", Integer.toString(extras.getInt("userId")));
        Log.d("UploadActivity/onResponse", titleET.getText().toString());
        Log.d("UploadActivity/onResponse", descET.getText().toString());
        Log.d("UploadActivity/onResponse", getTags().toString());

        int userId = extras.getInt("userId");
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), titleET.getText().toString());
        RequestBody desc = RequestBody.create(MediaType.parse("multipart/form-data"), descET.getText().toString());
        ArrayList<String> tags = getTags();

        Call<GeneralResponse> call = RetrofitClient.getInstance().getMyApi().uploadImage(userId, title, desc, tags, image);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    GeneralResponse generalResponse = response.body();
                    if(generalResponse.isSuccess())
                        Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("UploadActivity/onResponse","response broke");
                }

            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Log.e("onFailure error", t.getMessage());
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }

        });
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
                Toast.makeText(getApplicationContext(), "Already on upload page", Toast.LENGTH_LONG).show();
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