package com.example.chaq_sentiment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;


public class RegisterActivity extends AppCompatActivity {
    DBHelper DB = new DBHelper(this);
    EditText username ,pass,conpass,full_name,phone1;
    Button signup;
    TextView signin;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        username=(EditText) findViewById(R.id.username1);
        full_name=findViewById(R.id.name);
        pass=(EditText) findViewById(R.id.password1);
        conpass=(EditText) findViewById(R.id.repassword1);
        signup=(Button) findViewById(R.id.register1);
        signin=(TextView) findViewById(R.id.login1);
        phone1 = findViewById(R.id.phone);

        // Set max length including the "+91" prefix
        int maxLength = 13; // Length of "+91" is 3, and maximum digits for a phone number is typically 10
        phone1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        // Add TextWatcher to handle changes in the EditText
        phone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No implementation needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Ensure "+91" prefix remains at the start of the EditText
                if (!s.toString().startsWith("+91")) {
                    phone1.setText("+91");
                    Selection.setSelection(phone1.getText(), phone1.getText().length());
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user=username.getText().toString();
                String password=pass.getText().toString();
                String repassword=conpass.getText().toString();
                String name=full_name.getText().toString();
                String number=phone1.getText().toString();
                Random random=new Random();
                String id= String.valueOf(random.nextInt(999999999));
                if (user.isEmpty() || password.isEmpty() || repassword.isEmpty() || name.isEmpty() ||number.equals("")){
                    Toast.makeText(RegisterActivity.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }else
                if(password.equals(repassword)){
                    boolean checkuser=DB.checkuser(user);
                    if(!checkuser) {
                        boolean insert = DB.insertdata(id,user, password,name,number);
                        if (insert) {
                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registered Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "User Already Exits", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Password not Matching ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}