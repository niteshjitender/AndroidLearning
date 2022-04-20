package com.example.imagepickerwithdb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper {

    public static final String IMAGE_ID = "id" ;
    private final Context mContext ;
    public static final String IMAGE = "image" ;
    private DatabaseHelper mDbHelper ;
    private SQLiteDatabase mDb ;
    private static final String DATABASE_NAME = "Images.db" ;
    private static final int DATABASE_VERSION = 1;
    private static final String IMAGES_TABLE = "ImagesTable" ;
    private static final String CREATE_IMAGE_TABLE =
            "CREATE TABLE " + IMAGES_TABLE + "(" + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IMAGE + " BLOB NOT NULL);" ;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_IMAGE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IMAGES_TABLE );
            onCreate(sqLiteDatabase);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DbHelper(Context context){
        mContext = context ;
        mDbHelper = new DatabaseHelper(mContext) ;
    }

    public DbHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase() ;
        return this ;
    }

    public void close(){
        mDbHelper.close();
    }

    public void insertImage(byte []  imageBytes){
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(IMAGE,imageBytes) ;
        mDb.insert(IMAGES_TABLE,null,contentValues);
    }

    public byte []  retrieveImageFromDb(){
        Cursor cursor = mDb.query(true, IMAGES_TABLE, new String[]{IMAGE},
                null, null, null, null, IMAGE_ID + " Desc","1") ;
        if(cursor.moveToFirst()){
            @SuppressLint("Range") byte[] blob = cursor.getBlob(cursor.getColumnIndex(IMAGE));
            cursor.close() ;
            return  blob ;
        }
        cursor.close();
        return null ;
    }
}
