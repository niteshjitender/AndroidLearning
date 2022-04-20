package com.example.basicsqlwithimagepicker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper {

    public static final String USER_ID = "id" ;
    private final Context mContext ;
    public static final String IMAGE = "image" ;
    public static final String NAME = "name" ;
    public static final String CONTACT = "contact" ;
    public static final String DOB = "dob" ;
    private DatabaseHelper mDbHelper ;
    private SQLiteDatabase mDb ;
    private static final String DATABASE_NAME = "User.db" ;
    private static final int DATABASE_VERSION = 1;
    private static final String USER_TABLE = "UserTable" ;

    /** CREATE TABLE USER_TABLE(USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, IMAGE BLOB NOT NULL, NAME TEXT,
     * DOB TEXT) ;*/
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IMAGE + " BLOB NOT NULL, " + NAME + " TEXT, " + CONTACT + " TEXT, " + DOB + " TEXT);" ;


    /**SELECT * FROM USER_TABLE WHERE USER_ID = ?*/
    private static final String UPDATE_RAW_QUERY =
            "SELECT * FROM " + USER_TABLE + " WHERE " + USER_ID + " = ?" ;

    /**SELECT * FROM USER_TABLE WHERE USER_ID = ?*/
    private static final String DELETE_RAW_QUERY =
            "SELECT * FROM " + USER_TABLE + " WHERE " + USER_ID + " = ?" ;

    /**SELECT * FROM USER_TABLE */
    private static final String GET_DATA_RAW_QUERY =
            "SELECT * FROM " + USER_TABLE + " ORDER BY " + USER_ID + " DESC;" ;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE );
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


    public  Boolean insertUserData(byte[] imageBytes, String name, String contact, String dob){
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(IMAGE,imageBytes) ;
        contentValues.put(NAME, name) ;
        contentValues.put(CONTACT, contact) ;
        contentValues.put(DOB, dob) ;
        long result = mDb.insert(USER_TABLE,null,contentValues);
        if(result == -1){
            return false ;
        }
        else{
            return true ;
        }
    }

    public Boolean updateUserData(String id, byte[] imageBytes,String name, String contact, String dob){

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(IMAGE,imageBytes) ;
        contentValues.put(NAME, name) ;
        contentValues.put(CONTACT, contact) ;
        contentValues.put(DOB, dob) ;

        Cursor cursor = mDb.rawQuery(UPDATE_RAW_QUERY, new String[] {id}) ;
        if(cursor.getCount() > 0){
            long result = mDb.update(USER_TABLE, contentValues,USER_ID + " = ?", new String[] {id}) ;
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

    public Boolean deleteUserData(String id){

        Cursor cursor = mDb.rawQuery(DELETE_RAW_QUERY, new String[] {id}) ;
        if(cursor.getCount() > 0){
            long result = mDb.delete(USER_TABLE, USER_ID + " = ?", new String[] {id}) ;
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
        Cursor cursor = mDb.rawQuery(GET_DATA_RAW_QUERY, null) ;
        return cursor ;
    }
}
