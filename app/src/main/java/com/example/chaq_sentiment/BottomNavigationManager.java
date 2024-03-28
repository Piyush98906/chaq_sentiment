package com.example.chaq_sentiment;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationManager implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Activity activity;

    public BottomNavigationManager(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            activity.startActivity(new Intent(activity, HomeActivity.class));
            return true;
        } else if (id == R.id.reddit) {
            activity.startActivity(new Intent(activity, InputActivity.class));
            return true;
        } else if (id == R.id.text) {
             activity.startActivity(new Intent(activity, TestActivity.class));
            return true;
        } else if (id == R.id.feedback) {
            FeedbackActivity feedbackDialog = new FeedbackActivity(activity);
            feedbackDialog.setCancelable(false);
            feedbackDialog.show();
            return true;
        } else if (id == R.id.account) {
            activity.startActivity(new Intent(activity, DetailsActivity.class));
            return true;
        }

        return false;
    }
}

