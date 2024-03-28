package com.example.chaq_sentiment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class FeedbackActivity extends Dialog {
    private Float userrate= 0.0F;
    public FeedbackActivity(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        final AppCompatButton ratenow=findViewById(R.id.ratenowbtn);
        final AppCompatButton later=findViewById(R.id.laterbtn);
        final RatingBar ratingbar=findViewById(R.id.ratingbar);
        final ImageView image=findViewById(R.id.imageview);
        ratenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Thank You for Your Valuable Feedback", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating <=1){
                    image.setImageResource(R.drawable.angry);
                }
                else if (rating <=2){
                    image.setImageResource(R.drawable.two_star);
                }
                else if (rating <=3){
                    image.setImageResource(R.drawable.three_star);
                }
                else if (rating <=4){
                    image.setImageResource(R.drawable.four_star);
                }
                else if (rating <=5){
                    image.setImageResource(R.drawable.five_star);
                }
                animate(image);
                userrate=rating;
            }
        });
    }
    public void animate(ImageView image){
        ScaleAnimation scaleanimate=new
                ScaleAnimation(0,1f,0f,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleanimate.setFillAfter(true);
        scaleanimate.setDuration(200);
        image.startAnimation(scaleanimate);
    }
}
