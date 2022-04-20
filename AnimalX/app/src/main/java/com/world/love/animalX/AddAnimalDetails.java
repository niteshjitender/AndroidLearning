package com.world.love.animalX;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;

public class AddAnimalDetails extends AppCompatActivity {

    //Spinner related data
    private final String [] animalTypeSpinnerData = {AnimalType.DOMESTIC.toString(),AnimalType.WILD.toString()} ;
    private final String [] animalStatusSpinnerData = {AnimalStatus.IN_PROCESS.toString(), AnimalStatus.RELEASED.toString(), AnimalStatus.DEAD.toString()} ;

    //AnimalDetails variables
    private ImageView animalDetailsImageView ;
    private FloatingActionButton animalDetailsAddImageFloatingBtn ;
    private EditText animalDetailsAnimalName, animalDetailsAnimalDisease, animalDetailsAnimalLocation, animalDetailsAnimalReporter, animalDetailsAnimalReporterContact, animalDetailsAnimalRemarks, animalDetailsAnimalAge ;
    private Spinner animalDetailsAnimalTypeSpinner, animalDetailsAnimalStatusSpinner ;
    private Button animalDetailsSubmitButton, animalDetailsEntryDateButton, animalDetailsCompletionDateButton ;
    private LinearLayout animalDetailsEmptyDataLayout ;
    private TextView animalDetailsEntryDateTextView, animalDetailsCompletionDateTextView ;

    //For date
    private MaterialDatePicker.Builder materialEntryDateBuilder, materialCompletionDateBuilder ;
    private MaterialDatePicker materialEntryDatePicker, materialCompletionDatePicker ;

    //For database object
    AnimalDatabaseHelper dbHelper ;

    //Top level layout for add animals
    ConstraintLayout animalDetailsConstraintLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal_details);

        //Initializing the database
        dbHelper = new AnimalDatabaseHelper(this) ;

        //Top level layout
        animalDetailsConstraintLayout = findViewById(R.id.animalDetailsConstraintLayout) ;

        //Setting spinner values
        ArrayAdapter<String> arrayAdapterForAnimalType = new ArrayAdapter<String>(this,
                R.layout.spinner_layout,animalTypeSpinnerData) ;
        arrayAdapterForAnimalType.setDropDownViewResource( R.layout.spinner_layou_dropdown);
        animalDetailsAnimalTypeSpinner = findViewById(R.id.animalDetailsAnimalTypeSpinner) ;
        animalDetailsAnimalTypeSpinner.setAdapter(arrayAdapterForAnimalType);

        ArrayAdapter<String> arrayAdapterForAnimalStatus = new ArrayAdapter<String>(this,
                R.layout.spinner_layout,animalStatusSpinnerData) ;
        arrayAdapterForAnimalStatus.setDropDownViewResource( R.layout.spinner_layou_dropdown);
        animalDetailsAnimalStatusSpinner = findViewById(R.id.animalDetailsAnimalStatusSpinner) ;
        animalDetailsAnimalStatusSpinner.setAdapter(arrayAdapterForAnimalStatus);

        animalDetailsImageView = findViewById(R.id.animalDetailsImageView) ;
        animalDetailsAddImageFloatingBtn = findViewById(R.id.animalDetailsAddImageFloatingBtn) ;
        animalDetailsAnimalName = findViewById(R.id.animalDetailsAnimalName) ;
        animalDetailsAnimalDisease = findViewById(R.id.animalDetailsAnimalDisease) ;
        animalDetailsAnimalLocation = findViewById(R.id.animalDetailsAnimalLocation) ;
        animalDetailsAnimalReporter = findViewById(R.id.animalDetailsAnimalReporter) ;
        animalDetailsAnimalReporterContact = findViewById(R.id.animalDetailsAnimalReporterContact) ;
        animalDetailsAnimalRemarks = findViewById(R.id.animalDetailsAnimalRemarks) ;
        animalDetailsEntryDateTextView = findViewById(R.id.animalDetailsEntryDateTextView) ;
        animalDetailsCompletionDateTextView = findViewById(R.id.animalDetailsCompletionDateTextView) ;
        animalDetailsAnimalAge = findViewById(R.id.animalDetailsAnimalAge) ;

        //Buttons
        animalDetailsSubmitButton = findViewById(R.id.animalDetailsSubmitButton) ;
        animalDetailsEntryDateButton = findViewById(R.id.animalDetailsEntryDateButton) ;
        animalDetailsCompletionDateButton = findViewById(R.id.animalDetailsCompletionDateButton) ;

        //layout if no image is selected
        animalDetailsEmptyDataLayout = findViewById(R.id.animalDetailsEmptyDataLayout) ;
        if(animalDetailsImageView.getDrawable() == null){
            animalDetailsEmptyDataLayout.setVisibility(View.VISIBLE);
        }

        //Setting Title of Action bar
        ActionBar actionBar = getSupportActionBar() ;
        actionBar.setTitle("New Animal");

        //Disabling completion button
        animalDetailsCompletionDateButton.setEnabled(false);
        animalDetailsAddImageFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.hasStoragePermission(AddAnimalDetails.this)){
                    Utils.openImageChooser(AddAnimalDetails.this) ;
//                    int height = animalDetailsImageView.getHeight() ;
//                    int width = animalDetailsImageView.getWidth() ;
//                    Utils.showToast(AddAnimalDetails.this,height + " " + width);
                }
                else{
                    ActivityCompat.requestPermissions(((AppCompatActivity) AddAnimalDetails.this), new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 200);
                }
            }
        });

        animalDetailsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting image from imageView
                animalDetailsImageView.buildDrawingCache();
                Bitmap bitmap = animalDetailsImageView.getDrawingCache();
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream() ;
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteBuffer);

                //Getting details from ETLayout
                String animalNameTxt = Utils.getTextFromETLayout(animalDetailsAnimalName) ;
                String animalDiseaseTxt = Utils.getTextFromETLayout(animalDetailsAnimalDisease) ;
                String animalLocationTxt = Utils.getTextFromETLayout(animalDetailsAnimalLocation) ;
                String animalReporterTxt = Utils.getTextFromETLayout(animalDetailsAnimalReporter) ;
                String animalReporterContactTxt = Utils.getTextFromETLayout(animalDetailsAnimalReporterContact) ;
                String animalRemarksTxt = Utils.getTextFromETLayout(animalDetailsAnimalRemarks) ;
                String animalAgeTxt = Utils.getTextFromETLayout(animalDetailsAnimalAge) ;

                //Getting details from Spinners
                String animalTypeTxt = Utils.getValueFromSpinner(animalDetailsAnimalTypeSpinner) ;
                String animalStatusTxt = Utils.getValueFromSpinner(animalDetailsAnimalStatusSpinner) ;

                //Getting dates
                String animalEntryDateTxt = Utils.getTextTextView(animalDetailsEntryDateTextView) ;
                String animalEntryDateInMilliSeconds ;
                if(animalEntryDateTxt.equals("Entry Date"))
                    animalEntryDateInMilliSeconds = "";
                else
                    animalEntryDateInMilliSeconds = Utils.getMilliSecondDate(animalEntryDateTxt) ;
                String animalCompletionDateTxt = Utils.getTextTextView(animalDetailsCompletionDateTextView) ;
                String animalCompletionDateInMilliSeconds ;
                if(animalCompletionDateTxt.equals("Completion Date"))
                    animalCompletionDateInMilliSeconds = "";
                else
                    animalCompletionDateInMilliSeconds = Utils.getMilliSecondDate(animalCompletionDateTxt) ;

                try{
                    dbHelper.open() ;
                    byte [] animalImageBytes = byteBuffer.toByteArray();
                    boolean checkInsertData = dbHelper.insertAnimalData(animalNameTxt,animalTypeTxt,animalStatusTxt,
                            animalDiseaseTxt,animalLocationTxt,animalReporterTxt,
                            animalReporterContactTxt,animalRemarksTxt,animalImageBytes, animalEntryDateInMilliSeconds, animalCompletionDateInMilliSeconds, animalAgeTxt) ;
                    if(checkInsertData){
                        Utils.showToast(AddAnimalDetails.this, "New animal is inserted");
                        Intent intent = new Intent(AddAnimalDetails.this,  MainActivity.class) ;
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Utils.showToast(AddAnimalDetails.this,"New animal can not be inserted");
                    }
                    dbHelper.close();
                }
                catch (Exception e){
                    Utils.showToast(AddAnimalDetails.this,"Error!, new animal can not be inserted");
                    dbHelper.close();
                }
            }
        });

        //Getting datePicker
        materialEntryDateBuilder = Utils.datePicker("Select Entry Date");
        materialCompletionDateBuilder = Utils.datePicker("Select Completion Date");
        materialEntryDatePicker = materialEntryDateBuilder.build() ;

        animalDetailsEntryDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialEntryDatePicker.addOnPositiveButtonClickListener(
                                new MaterialPickerOnPositiveButtonClickListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onPositiveButtonClick(Object selection) {
                                        animalDetailsCompletionDateButton.setEnabled(true);
                                        animalDetailsEntryDateTextView.setText(materialEntryDatePicker.getHeaderText());
                                    }
                                });
                        materialEntryDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });


        animalDetailsCompletionDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!animalDetailsEntryDateTextView.getText().toString().equals("Entry Date")){
                            long entryDateMilliSeconds = Long.parseLong(Utils.getMilliSecondDate(animalDetailsEntryDateTextView.getText().toString().trim()));
                            CalendarConstraints constraintsBuilder =
                                    new CalendarConstraints.Builder()
                                            .setValidator(DateValidatorPointForward.from(entryDateMilliSeconds)).build();
                            materialCompletionDatePicker = materialEntryDateBuilder.setCalendarConstraints(constraintsBuilder).build();
                        }
                        else{
                            materialCompletionDatePicker = materialCompletionDateBuilder.build() ;
                        }
                        materialCompletionDatePicker.addOnPositiveButtonClickListener(
                                new MaterialPickerOnPositiveButtonClickListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onPositiveButtonClick(Object selection) {
                                        animalDetailsCompletionDateTextView.setText(materialCompletionDatePicker.getHeaderText());
                                    }
                                });
                        materialCompletionDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("WARNING, Entries are not saved!");
        builder.setMessage("Want to go back?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Backed successfully
//                AddAnimalDetails.super.onBackPressed();
                Intent intent = new Intent(AddAnimalDetails.this, MainActivity.class) ;
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Do nothing
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Utils.SELECT_PICTURE){
                Uri selectImageUri = data.getData() ;
                if(selectImageUri != null){
                    animalDetailsEmptyDataLayout.setVisibility(View.GONE);
                    animalDetailsImageView.setImageURI(selectImageUri);
                }
            }
        }
    }
}