package com.example.bookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ConcurrentModificationException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context ;
    private static final String DATABASE_NAME = "BookLibrary.db" ;
    private static final int DATABASE_VERSION = 1 ;
    private static final String TABLE_NAME = "myLibrary"  ;
    private static final String COLUMN_ID = "_id" ;
    private static final String COLUMN_TITLE = "book_title" ;
    private static final String COLUMN_AUTHOR = "book_author" ;
    private static final String COLUMN_PAGES = "book_pages" ;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context ;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /** create table table_name(column_id integer primary key autoincrement,
         column_title text, column_author text, column_pages integer) ;
         */
        String query = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, " + COLUMN_AUTHOR + " TEXT, " + COLUMN_PAGES + " TEXT);" ;

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    void addBook(String title, String author, Integer pages){
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put(COLUMN_TITLE, title) ;
        contentValues.put(COLUMN_AUTHOR, author) ;
        contentValues.put(COLUMN_PAGES, pages);
        
        long result = db.insert(TABLE_NAME, null, contentValues) ;
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Book Added", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME + " ;" ;
        SQLiteDatabase db = this.getReadableDatabase() ;

        Cursor cursor = null ;
        if(db != null) {
            cursor = db.rawQuery(query,null);
        }
        return cursor ;
    }

    void updateData(String rowId, String title, String author, String pages){

        SQLiteDatabase db = this.getWritableDatabase() ;

        ContentValues  contentValues = new ContentValues() ;
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_AUTHOR, author) ;
        contentValues.put(COLUMN_PAGES, pages) ;

        long result = db.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[] {rowId}) ;

        if(result == -1){
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();
        }
    }
    
    
    void deleteOneRow(String rowId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        long result = sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + "=?", new String [] {rowId}) ;
        if(result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME);
        Toast.makeText(context, "All Items are deleted", Toast.LENGTH_SHORT).show();
    }

}
