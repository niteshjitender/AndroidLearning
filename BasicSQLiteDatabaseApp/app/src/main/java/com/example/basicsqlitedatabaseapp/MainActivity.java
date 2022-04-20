package com.example.basicsqlitedatabaseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText name, contact, dob ;
    private Button insert, update, delete, view ;
    private DBHelper DB ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name) ;
        contact = findViewById(R.id.contact) ;
        dob = findViewById(R.id.dob) ;

        insert = findViewById(R.id.btnInsert) ;
        update = findViewById(R.id.btnUpdate) ;
        delete = findViewById(R.id.btnDelete) ;
        view = findViewById(R.id.btnView) ;

        DB = new DBHelper(this) ;

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameText = name.getText().toString().trim() ;
                String contactText = contact.getText().toString().trim() ;
                String dobText = dob.getText().toString().trim() ;

                Boolean checkInsertData = DB.insertUserData(nameText, contactText, dobText) ;
                if(checkInsertData){
                    Toast.makeText(MainActivity.this, "New entry inserted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "New entry is not inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String nameText = name.getText().toString().trim() ;
                String contactText = contact.getText().toString().trim() ;
                String dobText = dob.getText().toString().trim() ;

                Boolean checkInsertData = DB.updateUserData(nameText, contactText, dobText) ;
                if(checkInsertData){
                    Toast.makeText(MainActivity.this, "Entry updated", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Entry is not updated", Toast.LENGTH_SHORT).show();
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameText = name.getText().toString().trim() ;

                Boolean checkInsertData = DB.deleteUserData(nameText) ;
                if(checkInsertData){
                    Toast.makeText(MainActivity.this, "Entry deleted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Entry is not deleted", Toast.LENGTH_SHORT).show();
                }

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor res = DB.getData() ;
                if(res.getCount() == 0){
                    Toast.makeText(MainActivity.this, "No entry exists", Toast.LENGTH_SHORT).show();
                    return ;
                }
                StringBuffer buffer = new StringBuffer() ;
                while(res.moveToNext()){
                    buffer.append("Name : " + res.getString(0) + "\n") ;
                    buffer.append("Contact : " + res.getString(1) + "\n") ;
                    buffer.append("Date of Birth : " + res.getString(2) + "\n\n") ;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this) ;
                builder.setCancelable(true)  ;
                builder.setTitle("User Entries") ;
                builder.setMessage(buffer.toString()) ;
                builder.show() ;
            }
        });
    }
}