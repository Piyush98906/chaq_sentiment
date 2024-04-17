package com.example.chaq_sentiment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText user ,pass;
    Button login;
    TextView register;
    DBHelper DB;
    SharedPreferences sharedPref;
    CheckBox rememberMe;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        user=(EditText) findViewById(R.id.username1);
        pass=(EditText) findViewById(R.id.password1);
        login=(Button) findViewById(R.id.login);
        register = findViewById(R.id.register);
        rememberMe=findViewById(R.id.rem);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        sharedPref = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        // Check if "Remember Me" checkbox was previously checked
        boolean isRemembered = sharedPref.getBoolean("remembered", false);
        if (isRemembered) {
            String savedUsername = sharedPref.getString("username", "");
            user.setText(savedUsername);
            rememberMe.setChecked(true);
        }
        DB=new DBHelper(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String password = pass.getText().toString();
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "Please Enter All The Fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean chkusrpass = DB.checkusernamepassword(username, password);
                    if (chkusrpass) {
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);

                        // Save username to SharedPreferences if "Remember Me" checkbox is checked
                        if (rememberMe.isChecked()) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("remembered", true);
                            editor.putString("username", username);
                            editor.apply();
                        } else {
                            // Clear stored data if "Remember Me" checkbox is unchecked
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.clear().apply();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}