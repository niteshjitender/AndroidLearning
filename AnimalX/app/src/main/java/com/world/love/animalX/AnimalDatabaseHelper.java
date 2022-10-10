package com.world.love.animalX;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class AnimalDatabaseHelper {

    //Database Specific fields
    private final Context mContext ;
    private DBHelper mDbHelper ;
    private SQLiteDatabase mDb ;

    //Table fields
    private static String ANIMAL_CASE_ID = "case_id" ;
    private static String ANIMAL_NAME = "name" ;
    private static String ANIMAL_TYPE = "type" ;
    private static String ANIMAL_STATUS = "status" ;
    private static String ANIMAL_DISEASE = "disease" ;
    private static String ANIMAL_LOCATION = "location" ;
    private static String ANIMAL_REPORTER = "reporter" ;
    private static String ANIMAL_REPORTER_CONTACT = "reporterContact" ;
    private static String ANIMAL_REMARKS = "remarks" ;

    private static String ANIMAL_IMAGE = "animalImage" ;

    private static String ANIMAL_ENTRY_DATE = "entry_date" ;
    private static String ANIMAL_COMPLETION_DATE = "completion_date" ;

    private static String ANIMAL_AGE = "age";

    //Database details
    private static final String DATABASE_NAME = "animal.db" ;
    private static final int DATABASE_VERSION = 1;

    //Table
    private static final String ANIMAL_TABLE = "AnimalRecordTable" ;

    //Queries
    /** CREATE TABLE ANIMAL_TABLE(CASE_ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL, TYPE INTEGER NOT NULL, STATUS INTEGER NOT NULL,
     *  DISEASE TEXT NOT NULL, LOCATION TEXT NOT NULL, REPORTER TEXT NOT NULL, REPORTER_CONTACT TEXT NOT NULL, REMARKS TEXT,
     *  ANIMAL_IMAGE BLOB NOT NULL) ;
     */
    private static final String CREATE_ANIMAL_TABLE =
            "CREATE TABLE " + ANIMAL_TABLE + "( " + ANIMAL_CASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ANIMAL_NAME + " TEXT NOT NULL, " + ANIMAL_TYPE + " INTEGER NOT NULL, "
                    + ANIMAL_STATUS + " INTEGER NOT NULL, " + ANIMAL_DISEASE + " TEXT NOT NULL, " + ANIMAL_LOCATION + " TEXT NOT NULL, " + ANIMAL_REPORTER + " TEXT NOT NULL, " + ANIMAL_REPORTER_CONTACT +
                    " TEXT NOT NULL, " + ANIMAL_REMARKS + " TEXT, "  + ANIMAL_IMAGE + " BLOB NOT NULL, " + ANIMAL_ENTRY_DATE + " TEXT, " + ANIMAL_COMPLETION_DATE + " TEXT, " + ANIMAL_AGE + " TEXT);" ;


    /**SELECT * FROM ANIMAL_TABLE WHERE ANIMAL_CASE_ID = ?*/
    private static final String UPDATE_RAW_QUERY =
            "SELECT * FROM " + ANIMAL_TABLE + " WHERE " + ANIMAL_CASE_ID + " = ?" ;

    /**SELECT * FROM USER_TABLE WHERE ANIMAL_CASE_ID = ?*/
    private static final String DELETE_RAW_QUERY =
            "SELECT * FROM " + ANIMAL_TABLE + " WHERE " + ANIMAL_CASE_ID + " = ?" ;

    /**SELECT * FROM USER_TABLE WHERE ANIMAL_CASE_ID = ANIMAL_CASE_ID*/
    private static final String SELECT_BY_ID_QUERY =
            "SELECT * FROM " + ANIMAL_TABLE + " WHERE " + ANIMAL_CASE_ID + " = " ;

    /**SELECT * FROM USER_TABLE */
    private static final String GET_DATA_RAW_QUERY =
            "SELECT * FROM " + ANIMAL_TABLE + " ORDER BY " + ANIMAL_CASE_ID + " DESC;";


    public AnimalDatabaseHelper(Context context){
        mContext = context ;
        mDbHelper = new DBHelper(mContext) ;
    }

    public AnimalDatabaseHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase() ;
        return this ;
    }

    public void close(){
        mDbHelper.close();
    }



    //Inner Class
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_ANIMAL_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ANIMAL_TABLE );
            onCreate(sqLiteDatabase);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }


    //Database Specific CRUD functions
    public  Boolean insertAnimalData(String animalName, String animalType, String animalStatus, String animalDisease, String animalLocation, String animalReporter, String animalReporterContact,
                                   String animalRemarks, byte[] animalImageBytes, String animalEntryDateInMilliSeconds, String animalCompletionDateInMilliSeconds, String animalAge){
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(ANIMAL_NAME, animalName) ;
        contentValues.put(ANIMAL_TYPE, animalType) ;
        contentValues.put(ANIMAL_STATUS, animalStatus) ;
        contentValues.put(ANIMAL_DISEASE,animalDisease) ;
        contentValues.put(ANIMAL_LOCATION, animalLocation) ;
        contentValues.put(ANIMAL_REPORTER, animalReporter) ;
        contentValues.put(ANIMAL_REPORTER_CONTACT, animalReporterContact) ;
        contentValues.put(ANIMAL_REMARKS, animalRemarks) ;
        contentValues.put(ANIMAL_IMAGE, animalImageBytes) ;
        contentValues.put(ANIMAL_ENTRY_DATE, animalEntryDateInMilliSeconds) ;
        contentValues.put(ANIMAL_COMPLETION_DATE, animalCompletionDateInMilliSeconds) ;
        contentValues.put(ANIMAL_AGE, animalAge) ;

        Log.i("Database: ", "Inserting these values" + contentValues.toString());

        long result = mDb.insert(ANIMAL_TABLE,null,contentValues);
        if(result == -1){
            return false ;
        }
        else{
            return true ;
        }
    }

    public Boolean updateAnimalData(String animalCaseId, String animalName, String animalType, String animalStatus, String animalDisease, String animalLocation, String animalReporter, String animalReporterContact,
                                    String animalRemarks, byte[] animalImageBytes,String animalEntryDateInMilliSeconds, String animalCompletionDateInMilliSeconds, String animalAge){

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(ANIMAL_CASE_ID, animalCaseId);
        contentValues.put(ANIMAL_NAME, animalName) ;
        contentValues.put(ANIMAL_TYPE, animalType) ;
        contentValues.put(ANIMAL_STATUS, animalStatus) ;
        contentValues.put(ANIMAL_DISEASE,animalDisease) ;
        contentValues.put(ANIMAL_LOCATION, animalLocation) ;
        contentValues.put(ANIMAL_REPORTER, animalReporter) ;
        contentValues.put(ANIMAL_REPORTER_CONTACT, animalReporterContact) ;
        contentValues.put(ANIMAL_REMARKS, animalRemarks) ;
        contentValues.put(ANIMAL_IMAGE, animalImageBytes) ;
        contentValues.put(ANIMAL_ENTRY_DATE, animalEntryDateInMilliSeconds) ;
        contentValues.put(ANIMAL_COMPLETION_DATE, animalCompletionDateInMilliSeconds) ;
        contentValues.put(ANIMAL_AGE, animalAge) ;

        Log.i("Database: ", "Updating these values" + contentValues.toString());

        Cursor cursor = mDb.rawQuery(UPDATE_RAW_QUERY, new String[] {animalCaseId}) ;
        if(cursor.getCount() > 0){
            long result = mDb.update(ANIMAL_TABLE, contentValues,ANIMAL_CASE_ID + " = ?", new String[] {animalCaseId}) ;
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

    public Boolean deleteAnimalData(String animalCaseId){

        Cursor cursor = mDb.rawQuery(DELETE_RAW_QUERY, new String[] {animalCaseId}) ;

        Log.i("Database: ", "Deleting the animal data with case ID: {}", animalCaseId);

        if(cursor.getCount() > 0){
            long result = mDb.delete(ANIMAL_TABLE, ANIMAL_CASE_ID + " = ?", new String[] {animalCaseId}) ;
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

        Log.i("Database: ", "Getting animal data");
        
        return cursor ;
    }

    public Cursor getSingleAnimalDetails(String animalCaseId){

        Cursor cursor = mDb.rawQuery(SELECT_BY_ID_QUERY + animalCaseId, null) ;

        Log.i("Database: ", "Getting the animal data with case ID: {}", animalCaseId);
        
        return cursor ;
    }
}
