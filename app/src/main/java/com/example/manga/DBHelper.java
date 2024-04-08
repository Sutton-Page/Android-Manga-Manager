package com.example.manga;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context){

        super(context,"manga",null,1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String command = "create table manga(id integer primary key autoincrement, title text, url text)";
        db.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists manga");
        onCreate(db);

    }
}
