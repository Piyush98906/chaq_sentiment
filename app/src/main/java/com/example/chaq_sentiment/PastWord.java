package com.example.chaq_sentiment;

public class PastWord {
    private String word;
    private String timestamp;

    public PastWord(String word, String timestamp) {
        this.word = word;
        this.timestamp = timestamp;
    }

    public String getWord() {
        return word;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

