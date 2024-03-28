package com.example.chaq_sentiment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PastWordAdapter extends ArrayAdapter<PastWord> {

    private Context mContext;
    private List<PastWord> mPastWords;

    public PastWordAdapter(Context context, List<PastWord> pastWords) {
        super(context, 0, pastWords);
        mContext = context;
        mPastWords = pastWords;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.past_word_item, parent, false);
        }

        PastWord currentWord = mPastWords.get(position);

        TextView wordTextView = listItem.findViewById(R.id.word_text_view);
        wordTextView.setText(currentWord.getWord());

        TextView timestampTextView = listItem.findViewById(R.id.timestamp_text_view);
        timestampTextView.setText(currentWord.getTimestamp());

        return listItem;
    }
}
