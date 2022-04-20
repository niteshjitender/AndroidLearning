package com.world.love.animalX;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

//Color palette
//https://material.io/resources/color/#!/?view.left=0&view.right=1&primary.color=FEC0AA&secondary.color=042A2B&primary.text.color=000000&secondary.text.color=ffffff
public class MainActivity extends AppCompatActivity {

    CustomAdapter customAdapter ;
    RecyclerView animalRecyclerView ;
    FloatingActionButton floatingAddButton ;

    //For database object
    AnimalDatabaseHelper dbHelper ;

    //Arrays for adapters
    ArrayList  animalCardCaseIDArray, animalCardNameArray, animalCardTypeArray, animalCardStatusBarArray, animalCardShowStatusArray;
    ArrayList animalCardImageViewArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the database
        dbHelper = new AnimalDatabaseHelper(this) ;

        //RecyclerView of main activity to display animal cards
        animalRecyclerView = findViewById(R.id.animalRecyclerViewMainActivity) ;

        //To add animal details
        floatingAddButton = findViewById(R.id.floatingAddButton) ;

        floatingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddAnimalDetails.class) ;
                Log.i("Activities Movement", "MainActivity(Finished) to AddAnimalDetails") ;
                startActivity(intent);
                finish();
            }
        });

        //Initialising arrays
        animalCardCaseIDArray = new ArrayList<>() ;
        animalCardNameArray = new ArrayList<>() ;
        animalCardTypeArray = new ArrayList<>() ;
        animalCardStatusBarArray = new ArrayList<>() ;
        animalCardShowStatusArray = new ArrayList<>() ;
        animalCardImageViewArray = new ArrayList() ;

        //Filling the values in arrays from db
        storeDataInArrays() ;

        //setting values to adapters
        customAdapter = new CustomAdapter(MainActivity.this, MainActivity.this, animalCardImageViewArray, animalCardCaseIDArray,
                animalCardNameArray, animalCardTypeArray, animalCardStatusBarArray, animalCardShowStatusArray) ;
        Log.i("Internal", customAdapter.toString()) ;
        animalRecyclerView.setAdapter(customAdapter);
        animalRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    void storeDataInArrays() {
        dbHelper.open() ;
        Cursor res = dbHelper.getData() ;
        if(res.getCount() == 0){
            Utils.showToast(MainActivity.this, "No animal record found");
//            emptyDataImageView.setVisibility(View.VISIBLE);
//            noData.setVisibility(View.VISIBLE);
        }
        else{
//            emptyDataImageView.setVisibility(View.GONE);
//            noData.setVisibility(View.GONE);

            /** CREATE TABLE ANIMAL_TABLE(CASE_ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL, TYPE INTEGER NOT NULL, STATUS INTEGER NOT NULL,
             *  DISEASE TEXT NOT NULL, LOCATION TEXT NOT NULL, REPORTER TEXT NOT NULL, REPORTER_CONTACT TEXT NOT NULL, REMARKS TEXT,
             *  ANIMAL_IMAGE BLOB NOT NULL) ;
             */
            while(res.moveToNext()) {
                animalCardCaseIDArray.add(res.getString(0));
                animalCardNameArray.add(res.getString(1)) ;
                animalCardTypeArray.add(res.getString(2)) ;
                animalCardStatusBarArray.add(res.getString(3)) ;
                animalCardShowStatusArray.add(res.getString(3)) ;

                //Parsing and storing image
                @SuppressLint("Range") byte[] blob = res.getBlob(9) ;
                animalCardImageViewArray.add(Utils.getImage(blob)) ;
            }
        }
        dbHelper.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("Activities Movement", "Get Back to MainActivity after getting result with requestCode: " + requestCode + ", resultCode: " + resultCode + ", Inent data:" + data) ;
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Utils.EDIT_DETAILS){
//                Utils.showToast(this, "This activity is restarted");
                recreate();
            }
        }
    }

    //Customizing menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater() ;
        inflater.inflate(R.menu.my_menu, menu);
        //For setting the textColors of menu items & icons
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
            item.setIcon(getDrawable(R.drawable.ic_pdf_24));
        }
        return super.onCreateOptionsMenu(menu);
    }

    //Item selection action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.exportPDF) {
//            confirmDialog();
            Utils.showToast(this,"Exporting");
        }
        return super.onOptionsItemSelected(item);
    }
}