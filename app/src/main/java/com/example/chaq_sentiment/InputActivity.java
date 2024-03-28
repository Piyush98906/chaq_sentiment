package com.example.chaq_sentiment;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InputActivity extends AppCompatActivity {
    TextInputEditText wordEditText;
    private DBHelper dbHelper;
    private ListView pastWordsListView;
    Python py;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        wordEditText = findViewById(R.id.word_edit_text);
        Button submitButton = findViewById(R.id.submit_button);
        pastWordsListView=findViewById(R.id.past_words_list);
        TextView logging = findViewById(R.id.loging);
        dbHelper=new DBHelper(this);
        final CustomProgressDialog dialog=new CustomProgressDialog(InputActivity.this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String word = wordEditText.getText().toString();
                dialog.show();
                if (TextUtils.isEmpty(word)) {
                    // Display a toast message if the word is empty
                    Toast.makeText(InputActivity.this, "Please enter a word", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                // Perform the following operations asynchronously
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            if (!Python.isStarted()) {
                                Python.start(new AndroidPlatform(InputActivity.this));
                            }
                            py = Python.getInstance();
                            PyObject pyObject = py.getModule("sentiment_analysis");
                            PyObject result = pyObject.callAttr("perform_sentiment_analysis", word);
                            List<PyObject> headlinesList = result.asList();

                            // Extract positive and negative headlines separately
                            PyObject positiveHeadlinesObj = headlinesList.get(0);
                            PyObject neutralHeadlinesObj = headlinesList.get(1);
                            PyObject negativeHeadlinesObj = headlinesList.get(2);

                            // Convert positive and negative headlines to List of Strings
                            List<String> positiveHeadlines = new ArrayList<>();
                            for (PyObject obj : positiveHeadlinesObj.asList()) {
                                positiveHeadlines.add(obj.toString());
                            }

                            List<String> negativeHeadlines = new ArrayList<>();
                            for (PyObject obj : negativeHeadlinesObj.asList()) {
                                negativeHeadlines.add(obj.toString());
                            }
                            List<String> neutralHeadlines = new ArrayList<>();
                            for (PyObject obj : neutralHeadlinesObj.asList()) {
                                neutralHeadlines.add(obj.toString());
                            }

                            // Insert past word into the database
                            SharedPreferences sharedPref = getSharedPreferences("send", Context.MODE_PRIVATE);
                            String username = sharedPref.getString("username", null);
                            dbHelper.insertPastWord(username, word);

                            // Navigate to OutputActivity with the results
                            Intent intent = new Intent(getApplicationContext(), OutputActivity.class);
                            // Pass positive and negative headlines as extras to the intent
                            intent.putStringArrayListExtra("positive_headlines", new ArrayList<>(positiveHeadlines));
                            intent.putStringArrayListExtra("negative_headlines", new ArrayList<>(negativeHeadlines));
                            intent.putStringArrayListExtra("neutral_headlines", new ArrayList<>(neutralHeadlines));
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.e("Error occurred: ", e.getMessage(), e);
                            // Handle any exceptions that occur during the process
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InputActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        dialog.dismiss();
                    }
                }.execute();
            }
        });

        logging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InputActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationManager bottomNavigationManager = new BottomNavigationManager(this);
        // Set listener
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationManager);
    }
    @Override
    protected void onResume() {
        super.onResume();
        displayPastWords();
    }


    private void displayPastWords() {
        SharedPreferences sharedPref = getSharedPreferences("send", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        List<PastWord> pastWords = dbHelper.getAllPastWords(username);
        PastWordAdapter adapter = new PastWordAdapter(this, pastWords);
        pastWordsListView.setAdapter(adapter);
    }

}