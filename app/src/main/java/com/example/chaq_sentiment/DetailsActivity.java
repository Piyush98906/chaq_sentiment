package com.example.chaq_sentiment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.DatagramPacket;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity{
    TextView phone, username,pass, fullname ,logout,idtext;
    ImageView imageView;
    Button btn;
    SharedPreferences sharedPreferences;
    private String loggedInUsername;

    private static final String PREF_IMAGE_URI_PREFIX = "image_uri_";
    private static final String PREF_IMAGE_URI = "pref_image_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details); // Set the layout file
        btn = findViewById(R.id.update);
        imageView = findViewById(R.id.imageView);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.user);
        logout = findViewById(R.id.log); // Initialize the logout TextView
        fullname = findViewById(R.id.FullName);
        idtext=findViewById(R.id.iddetails);
        pass=findViewById(R.id.PASS);
        DBHelper dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationManager bottomNavigationManager = new BottomNavigationManager(this);

//        Objects.requireNonNull(getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.dialog1))));
        // Set listener
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationManager);
        SharedPreferences sharedPref = getSharedPreferences("send", Context.MODE_PRIVATE);
        String username1 = sharedPref.getString("username", null);
        loggedInUsername = sharedPreferences.getString("username", null);

        //Travel through database
        Cursor cursor = dbHelper.getData(username1);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String fullNameText = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String phoneText = cursor.getString(cursor.getColumnIndex("number"));
            @SuppressLint("Range") String passwordtext = cursor.getString(cursor.getColumnIndex("password"));
            @SuppressLint("Range") String usernameText = cursor.getString(cursor.getColumnIndex("username"));
            fullname.setText(fullNameText);
            phone.setText(phoneText);
            username.setText(usernameText);
            pass.setText(passwordtext);
            idtext.setText(id);
            String imageUriKey = PREF_IMAGE_URI_PREFIX + loggedInUsername;
            String imageUri = sharedPreferences.getString(PREF_IMAGE_URI, null);
            if (imageUri != null) {
                Uri uri = Uri.parse(imageUri);
                imageView.setImageURI(uri);
            }
            cursor.close();
        } else {
            Toast.makeText(this, "No data found for this username", Toast.LENGTH_SHORT).show();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(DetailsActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(intent);
                String imageUriKey = PREF_IMAGE_URI_PREFIX + loggedInUsername;
                sharedPreferences.edit().remove(imageUriKey).apply();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            // Handle image selection result
            Uri uri = data.getData();
            if (uri != null) {
                // Save image URI to SharedPreferences with the logged-in username as the key
                String imageUriKey = PREF_IMAGE_URI_PREFIX + loggedInUsername;
                sharedPreferences.edit().putString(imageUriKey, uri.toString()).apply();
                imageView.setImageURI(uri);
            }
        }
    }
}