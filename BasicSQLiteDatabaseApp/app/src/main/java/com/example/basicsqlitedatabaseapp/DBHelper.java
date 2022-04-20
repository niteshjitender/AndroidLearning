package com.example.basicsqlitedatabaseapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "UserData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table UserDetails(name text primary key, contact text, dob text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists UserDetails");
    }

    public Boolean insertUserData(String name, String contact, String dob){

        SQLiteDatabase DB = this.getWritableDatabase() ;

        ContentValues contentValues = new ContentValues() ;
        contentValues.put("name", name) ;
        contentValues.put("contact", contact) ;
        contentValues.put("dob", dob) ;

        long result = DB.insert("UserDetails", null, contentValues) ;
        if(result == -1){
            return false ;
        }
        else{
            return true ;
        }

    }

    public Boolean updateUserData(String name, String contact, String dob){

        SQLiteDatabase DB = this.getWritableDatabase() ;

        ContentValues contentValues = new ContentValues() ;
        contentValues.put("name", name) ;
        contentValues.put("contact", contact) ;
        contentValues.put("dob", dob) ;

        Cursor cursor = DB.rawQuery("select * from UserDetails where name = ?", new String[] {name}) ;
        if(cursor.getCount() > 0){
            long result = DB.update("UserDetails", contentValues,"name = ?", new String[] {name}) ;
            if(result == -1){
                return false ;
            }
            else{
                return true ;
            }
        }
        else{
            return false ;
        }

    }

    public Boolean deleteUserData(String name){

        SQLiteDatabase DB = this.getWritableDatabase() ;

        ContentValues contentValues = new ContentValues() ;
        contentValues.put("name", name) ;

        Cursor cursor = DB.rawQuery("select * from UserDetails where name = ?", new String[] {name}) ;
        if(cursor.getCount() > 0){
            long result = DB.delete("UserDetails", "name = ?", new String[] {name}) ;
            if(result == -1){
                return false ;
            }
            else{
                return true ;
            }
        }
        else{
            return false ;
        }

    }


    public Cursor getData(){

        SQLiteDatabase DB = this.getWritableDatabase() ;
        Cursor cursor = DB.rawQuery("select * from UserDetails", null) ;
        return cursor ;
    }
}
