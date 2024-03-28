package com.example.chaq_sentiment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OutputActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationManager bottomNavigationManager;
    private TextView positiveHeadlinesTextView, negativeHeadlinesTextView,neutralheadlines;
    private ImageView plotImageView;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        Button button = findViewById(R.id.loader);
        positiveHeadlinesTextView = findViewById(R.id.tp);
        negativeHeadlinesTextView = findViewById(R.id.tn);
        neutralheadlines=findViewById(R.id.tneutral);
        plotImageView = findViewById(R.id.plot_image_view);

        // Receive results (positive and negative headlines) from InputActivity
        Intent intent = getIntent();
        List<String> neutralHeadlines = intent.getStringArrayListExtra("neutral_headlines");
        List<String> positiveHeadlines = intent.getStringArrayListExtra("positive_headlines");
        List<String> negativeHeadlines = intent.getStringArrayListExtra("negative_headlines");


// Display positive and negative headlines
        displayHeadlines(neutralHeadlines,positiveHeadlines, negativeHeadlines);

// Load plot.png into ImageView
        loadPlotImage();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPlotImage();
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationManager = new BottomNavigationManager(this);

        // Set listener
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationManager);
    }

    private void displayHeadlines(List<String> positiveHeadlines,List<String>neutralHeadlines, List<String> negativeHeadlines) {
        // Display positive headlines
        TextView positiveHeadlinesTextView = findViewById(R.id.tp);
        StringBuilder positiveBuilder = new StringBuilder();
        for (String headline : positiveHeadlines) {
            positiveBuilder.append(headline).append("\n\n");
        }
        positiveHeadlinesTextView.setText(positiveBuilder.toString());

        // Display negative headlines
        TextView negativeHeadlinesTextView = findViewById(R.id.tn);
        StringBuilder negativeBuilder = new StringBuilder();
        for (String headline : negativeHeadlines) {
            negativeBuilder.append(headline).append("\n\n");
        }
        negativeHeadlinesTextView.setText(negativeBuilder.toString());
        // Display negative headlines
        TextView neutralheadlines = findViewById(R.id.tneutral);
        StringBuilder neutralBuilder = new StringBuilder();
        for (String headline : neutralHeadlines) {
            neutralBuilder.append(headline).append("\n\n");
        }
        neutralheadlines.setText(neutralBuilder.toString());
    }




    private void loadPlotImage() {
        // Create a custom AlertDialog with the plot image
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_plot_image, null);
        ImageView plotImageView = dialogView.findViewById(R.id.plot_image_view);

        // Load the image from the specified file path
        Bitmap bitmap = BitmapFactory.decodeFile("/data/user/0/com.example.chaq_sentiment/files/plot.png");
        if (bitmap != null) {
            plotImageView.setImageBitmap(bitmap);
        } else {
            // Handle case when image loading fails
            // For example, display a placeholder image or show an error message

            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            // You can also display a placeholder image
            // plotImageView.setImageResource(R.drawable.placeholder_image);
        }

        builder.setView(dialogView);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
