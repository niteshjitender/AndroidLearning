package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText title_input, author_input, pages_input ;
    Button add_book_button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input_update) ;
        author_input = findViewById(R.id.author_input_update) ;
        pages_input = findViewById(R.id.pages_input_update) ;

        add_book_button = findViewById(R.id.update_book_button);

        add_book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper myDB = new DatabaseHelper(AddActivity.this);
                myDB.addBook(title_input.getText().toString().trim(),
                            author_input.getText().toString().trim(),
                            Integer.valueOf(pages_input.getText().toString().trim()));
                Intent intent = new Intent(AddActivity.this, MainActivity.class) ;
                startActivity(intent);
                finish();
            }
        });
    }
}