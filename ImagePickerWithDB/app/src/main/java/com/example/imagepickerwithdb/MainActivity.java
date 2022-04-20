package com.example.imagepickerwithdb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int  SELECT_PICTURE = 100 ;
    private static final String TAG = "MainActivity" ;
    ConstraintLayout constraintLayout ;
    ImageView imageView ;
    FloatingActionButton btnSelectImage ;
    DbHelper dbHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraintLayout) ;
        btnSelectImage = findViewById(R.id.btnSelectImage) ;
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Uri selectImageUri = data.getData() ;
                if(selectImageUri != null){
                    if(saveImageInDb(selectImageUri)){
                        showMessage("Image saved in Database successfully");
                        imageView.setImageURI(selectImageUri);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setVisibility(View.GONE);
                            }
                        }, 2000) ;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(loadImageFromDb()){
                                    imageView.setVisibility(View.VISIBLE);
                                }
                            }
                        },3500) ;
                    }
                }
            }
        }
    }

    private boolean loadImageFromDb() {
        try{
            dbHelper.open() ;
            byte [] bytes = dbHelper.retrieveImageFromDb() ;
            dbHelper.close();
            imageView.setImageBitmap(Utils.getImage(bytes));
            return true ;
        }
        catch (Exception e){
            dbHelper.close();
            return false ;
        }
    }

    private boolean saveImageInDb(Uri selectImageUri) {
        try{
            dbHelper.open() ;
            InputStream inputStream = getContentResolver().openInputStream(selectImageUri) ;
            byte [] inputData = Utils.getBytes(inputStream) ;
            dbHelper.insertImage(inputData);
            dbHelper.close(); ;
            return true ;
        }
        catch (Exception e){
            dbHelper.close();
            return false ;
        }
    }

    private void openImageChooser() {
        //Using the library
        ImagePicker.with(this)
                .crop(16f, 9f)	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(SELECT_PICTURE) ;

//        Intent intent = new Intent() ;
//        intent.setType("image/*") ;
//        intent.setAction(Intent.ACTION_GET_CONTENT) ;
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    private boolean hasStoragePermission(Context context) {
        int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) ;
        return read == PackageManager.PERMISSION_GRANTED ;
    }

    void showMessage(String message){
        Snackbar snackbar = Snackbar.make(constraintLayout, message, Snackbar.LENGTH_LONG) ;
        snackbar.show();
    }



}