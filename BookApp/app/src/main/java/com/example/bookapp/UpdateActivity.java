package com.example.bookapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText title_input_update, author_input_update, pages_input_update ;
    Button update_button, delete_button ;
    String id, title, author, pages ;
    DatabaseHelper myDB ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        myDB = new DatabaseHelper(UpdateActivity.this) ;

        title_input_update = findViewById(R.id.title_input_update) ;
        author_input_update = findViewById(R.id.author_input_update) ;
        pages_input_update = findViewById(R.id.pages_input_update) ;

        getAndSetIntentData() ;

        ActionBar actionBar = getSupportActionBar() ;
        actionBar.setTitle(title);

        update_button = findViewById(R.id.update_book_button) ;
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = title_input_update.getText().toString().trim();
                author = author_input_update.getText().toString().trim();
                pages = pages_input_update.getText().toString().trim();
                myDB.updateData(id, title, author, pages);
                finish();

            }
        });

        delete_button = findViewById(R.id.delete_book_button) ;
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });


    }

    void getAndSetIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("author") && getIntent().hasExtra("pages")){
            id = getIntent().getStringExtra("id") ;
            title = getIntent().getStringExtra("title") ;
            author = getIntent().getStringExtra("author") ;
            pages = getIntent().getStringExtra("pages") ;
            title_input_update.setText(title);
            author_input_update.setText(author);
            pages_input_update.setText(pages);
        }
        else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this) ;
        builder.setTitle("Delete " + title ) ;
        builder.setMessage("Are you sure want to delete " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDB.deleteOneRow(id) ;
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}