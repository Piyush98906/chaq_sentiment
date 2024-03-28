package com.example.chaq_sentiment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME="Login.db";

    public DBHelper( Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key,password TEXT,name TEXT,number TEXT)");
        MyDB.execSQL("create Table past_words(username TEXT, word TEXT, date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists past_words");
        onCreate(MyDB);
    }

    public boolean insertdata(String username,String password,String name,String number){
        SQLiteDatabase MyDB=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("username",username);
        cv.put("password",password);
        cv.put("number", number);
        cv.put("name",name);
        long res=MyDB.insert("users",null,cv);
        if(res==-1){
            return false;
        }
        else
            return true;
    }

    public boolean checkuser(String username) {
        SQLiteDatabase MyDB=this.getWritableDatabase();
        Cursor cus =MyDB.rawQuery("select * from users where username=?",new String[]{username});
        if (cus.getCount()>0){
            return true;
        }else
            return false;
    }
    public boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB =this.getWritableDatabase();
        Cursor cus =MyDB.rawQuery("select * from users where username=? and password=?",new String[]{username,password});
        if (cus.getCount()>0){
            return true;
        }else
            return false;
    }
    public Cursor getData(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.rawQuery("select name, number,username,password from users where username=?", new String[]{username});
    }
    public boolean insertPastWord(String username, String word) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);  // Set the username
        cv.put("word", word);
        long res = MyDB.insert("past_words", null, cv);
        MyDB.close();
        return res != -1;
    }


    public List<PastWord> getAllPastWords(String username) {
        List<PastWord> pastWords = new ArrayList<>();
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT word, strftime('%H:%M', date_added)FROM past_words WHERE username = ?", new String[]{username});
        // HashMap to store word and its latest timestamp
        HashMap<String, String> wordMap = new HashMap<>();

        // Loop through the cursor to populate the wordMap
        if (cursor.moveToFirst()) {
            do {
                String word = cursor.getString(0);
                String timestamp = cursor.getString(1);
                wordMap.put(word, timestamp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        MyDB.close();

        // Create PastWord objects from the wordMap
        for (Map.Entry<String, String> entry : wordMap.entrySet()) {
            String word = entry.getKey();
            String timestamp = entry.getValue();
            PastWord pastWord = new PastWord(word, timestamp);
            pastWords.add(pastWord);
        }

        return pastWords;
    }

}