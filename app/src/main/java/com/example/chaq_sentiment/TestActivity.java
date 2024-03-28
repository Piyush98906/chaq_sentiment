package com.example.chaq_sentiment;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
public class TestActivity extends AppCompatActivity {
    Typeface customFont; // Declare the custom font field
    EditText textInput;
    Button analyzeButton;
    PieChart pieChart;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationManager bottomNavigationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        customFont = ResourcesCompat.getFont(this, R.font.seg_ui_semibold);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv=findViewById(R.id.senti);
        textInput = findViewById(R.id.text_input);
        analyzeButton = findViewById(R.id.analyze_button);
        pieChart=findViewById(R.id.pieChart);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationManager = new BottomNavigationManager(this);
        // Set listener

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationManager);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textInput.getText().toString();
                CustomProgressDialog dialog = new CustomProgressDialog(TestActivity.this);
                dialog.show();
                pieChart.setVisibility(View.VISIBLE);
                Python py = Python.getInstance();
                PyObject pyObject = py.getModule("text_analysis");
                PyObject pyResult = pyObject.callAttr("analyze_sentiment", text);

                float positivePercent = pyResult.asList().get(0).toFloat();
                float neutralPercent = pyResult.asList().get(1).toFloat();
                float negativePercent = pyResult.asList().get(2).toFloat();
                tv.setText("Sentiment Analysis");
                setupPieChart(positivePercent, neutralPercent, negativePercent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 1500);
            }
        });
    }
    private void setupPieChart(float positivePercent, float neutralPercent, float negativePercent) {
            ArrayList<PieEntry> entries = new ArrayList<>();
        if (positivePercent > 0) {
            entries.add(new PieEntry(positivePercent, "Positive"));
        }
        if (neutralPercent > 0) {
            entries.add(new PieEntry(neutralPercent, "Neutral"));
        }
        if (negativePercent > 0) {
            entries.add(new PieEntry(negativePercent, "Negative"));
        }

            PieDataSet dataSet = new PieDataSet(entries, " ");
            int[] colors = new int[] {
                    Color.rgb(144, 238, 144),  // Light Green for Positive
                    Color.rgb(255, 165, 0),     // Tomato Red for Negative
                    Color.rgb(255, 99, 71)      // Orange for Neutral
            };
            dataSet.setColors(colors);
            dataSet.setValueTextColor(Color.BLACK);  // Set value text color
            dataSet.setValueTextSize(14f);  // Set value text size
            dataSet.setValueFormatter(new PercentFormatter(pieChart));  // Format percentage values
            dataSet.setSliceSpace(3f);  // Add some space between slices
            PieData pieData = new PieData(dataSet);
            pieChart.setData(pieData);
            pieChart.animateY(1000,Easing.EaseInCubic);
            pieChart.setEntryLabelTypeface(customFont);
            pieChart.setCenterTextSize(16f);
            pieChart.setUsePercentValues(true);  // Show percentage values
            pieChart.setEntryLabelTextSize(14f);// Set entry label text size
            pieChart.setRotationEnabled(true); // Enable rotation for the PieChart
        pieChart.setUsePercentValues(true);  // Show percentage values
            pieChart.setEntryLabelColor(Color.WHITE);  // Set entry label color
            pieChart.getDescription().setEnabled(false);  // Disable description
            pieChart.setRotationEnabled(true);  // Enable rotation of the chart by touch
            pieChart.setDrawHoleEnabled(false);// Move legend above the chart
            Legend legend = pieChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setDrawInside(false);
            legend.setTextSize(12f);
            pieChart.setHighlightPerTapEnabled(true);// Enable seamless highlighting
            pieChart.invalidate(); //Refresh chart
    }
}