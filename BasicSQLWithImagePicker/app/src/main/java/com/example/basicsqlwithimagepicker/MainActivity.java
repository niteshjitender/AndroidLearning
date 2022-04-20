package com.example.basicsqlwithimagepicker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int  SELECT_PICTURE = 100 ;
    private static final String TAG = "MainActivity" ;
    RelativeLayout relativeLayout ;
    ImageView imageView ;
    FloatingActionButton btnSelectImage ;
    Button btnInsert, btnDelete, btnUpdate, btnView ;
    private EditText id, name, contact, dob ;
    DbHelper dbHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this) ;

        relativeLayout = findViewById(R.id.relativeLayout) ;

        id = findViewById(R.id.userId) ;
        name = findViewById(R.id.name) ;
        contact = findViewById(R.id.contact) ;
        dob = findViewById(R.id.dob) ;

        btnSelectImage = findViewById(R.id.btnSelectImage) ;
        btnDelete = findViewById(R.id.btnDelete) ;
        btnView = findViewById(R.id.btnView) ;
        btnInsert = findViewById(R.id.btnInsert) ;
        btnUpdate = findViewById(R.id.btnUpdate) ;
        imageView = findViewById(R.id.imageView) ;

        dbHelper = new DbHelper(this) ;

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasStoragePermission(MainActivity.this)){
                    openImageChooser() ;
                }
                else{
                    ActivityCompat.requestPermissions(((AppCompatActivity) MainActivity.this), new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 200);
                }
            }
        });


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream() ;
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteBuffer);
                String nameText = name.getText().toString().trim() ;
                String contactText = contact.getText().toString().trim() ;
                String dobText = dob.getText().toString().trim() ;
                try{
                    dbHelper.open() ;
                    byte [] inputData = byteBuffer.toByteArray();;
                    boolean checkInsertData = dbHelper.insertUserData(inputData,nameText,contactText,dobText) ;
                    if(checkInsertData){
                        showMessage("New user is inserted");
                    }
                    else{
                        showMessage("New user can not be inserted");
                    }
                    dbHelper.close();
                }
                catch (Exception e){
                    showMessage("Error!, new user can not be inserted");
                    dbHelper.close();
                }

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idText = id.getText().toString().trim() ;
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream() ;
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteBuffer);
                String nameText = name.getText().toString().trim() ;
                String contactText = contact.getText().toString().trim() ;
                String dobText = dob.getText().toString().trim() ;
                try{
                    dbHelper.open() ;
                    byte [] inputData = byteBuffer.toByteArray();;
                    Boolean checkUpdateData = dbHelper.updateUserData(idText,inputData,nameText,contactText,dobText) ;
                    if(checkUpdateData){
                        showMessage("User is updated");
                    }
                    else{
                        showMessage("New user can not be updated");
                    }
                    dbHelper.close();
                }
                catch (Exception e){
                    showMessage("Error!, new user can not be updated");
                    dbHelper.close();
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idText = id.getText().toString().trim() ;
                try{
                    dbHelper.open() ;;
                    Boolean checkDeleteData = dbHelper.deleteUserData(idText) ;
                    if(checkDeleteData){
                        showMessage("User is deleted");
                    }
                    else{
                        showMessage("New user can not be deleted");
                    }
                    dbHelper.close();
                }
                catch (Exception e){
                    showMessage("Error!, new user can not be deleted");
                    dbHelper.close();
                }
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                dbHelper.open() ;
                Cursor res = dbHelper.getData() ;
                if(res.getCount() == 0){
                    Toast.makeText(MainActivity.this, "No entry exists", Toast.LENGTH_SHORT).show();
                    return ;
                }
                StringBuffer buffer = new StringBuffer() ;
                while(res.moveToNext()){
                    @SuppressLint("Range") byte[] blob = res.getBlob(res.getColumnIndex("image"));
                    buffer.append("Id : " + res.getString(res.getColumnIndex("id")) + "\n") ;
                    buffer.append("Name : " + res.getString(res.getColumnIndex("name")) + "\n") ;
                    buffer.append("Contact : " + res.getString(res.getColumnIndex("contact")) + "\n") ;
                    buffer.append("Date of Birth : " + res.getString(res.getColumnIndex("dob")) + "\n\n") ;
                    imageView.setImageBitmap(Utils.getImage(blob));

                }
                res.close();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this) ;
                builder.setCancelable(true)  ;
                builder.setTitle("User Entries") ;
                builder.setMessage(buffer.toString()) ;
                builder.show() ;
                dbHelper.close();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Uri selectImageUri = data.getData() ;
                if(selectImageUri != null){
                    imageView.setImageURI(selectImageUri);
                }
            }
        }
    }

    private void openImageChooser() {
        //Using the library
        ImagePicker.with(this)
                .crop(16f, 9f)	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(SELECT_PICTURE) ;

    }

    private boolean hasStoragePermission(Context context) {
        int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) ;
        return read == PackageManager.PERMISSION_GRANTED ;
    }

    void showMessage(String message){
        Snackbar snackbar = Snackbar.make(relativeLayout, message, Snackbar.LENGTH_LONG) ;
        snackbar.show();
    }



}