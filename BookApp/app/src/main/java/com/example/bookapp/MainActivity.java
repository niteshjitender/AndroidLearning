package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    private FloatingActionButton add_button ;

    DatabaseHelper databaseHelper ;
    ArrayList <String> book_id, book_title, book_author, book_pages ;
    CustomAdapter customAdapter ;
    ImageView emptyDataImageView ;
    TextView noData ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView) ;
        add_button = findViewById(R.id.add_button) ;
        emptyDataImageView = findViewById(R.id.emptyDataImageView) ;
        noData = findViewById(R.id.noData)  ;

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class) ;
                startActivity(intent);
                finish();
            }
        });

        databaseHelper = new DatabaseHelper(MainActivity.this) ;

        book_id = new ArrayList<>() ;
        book_title = new ArrayList<>() ;
        book_author = new ArrayList<>() ;
        book_pages = new ArrayList<>() ;

        storeDataInArrays() ;

        customAdapter = new CustomAdapter(MainActivity.this, MainActivity.this, book_id, book_title, book_author, book_pages) ;
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    void storeDataInArrays() {
        Cursor cursor = databaseHelper.readAllData() ;
        if(cursor.getCount() == 0){
//            Toast.makeText(this, "No data present", Toast.LENGTH_SHORT).show();
            emptyDataImageView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.VISIBLE);
        }
        else{
            emptyDataImageView.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            while(cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1)) ;
                book_author.add(cursor.getString(2)) ;
                book_pages.add(cursor.getString(3)) ;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater() ;
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        builder.setTitle("Delete All") ;
        builder.setMessage("Are you sure want to delete All ?" );
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseHelper.deleteAllData() ;
//                Intent intent = new Intent(MainActivity.this, MainActivity.class) ;
//                startActivity(intent);
//                finish() ;
                recreate();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}