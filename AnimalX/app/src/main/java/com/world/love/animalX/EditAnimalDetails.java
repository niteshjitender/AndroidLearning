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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;

public class EditAnimalDetails extends AppCompatActivity {

    //Spinner related data
    private final String [] animalTypeSpinnerData = {AnimalType.DOMESTIC.toString(),AnimalType.WILD.toString()} ;
    private final String [] animalStatusSpinnerData = {AnimalStatus.IN_PROCESS.toString(), AnimalStatus.RELEASED.toString(), AnimalStatus.DEAD.toString()} ;

    //AnimalDetails variables
    private ImageView editAnimalDetailsImageView ;
    private FloatingActionButton editAnimalDetailsAddImageFloatingBtn ;
    private EditText editAnimalDetailsAnimalName, editAnimalDetailsAnimalDisease, editAnimalDetailsAnimalLocation, editAnimalDetailsAnimalReporter, editAnimalDetailsAnimalReporterContact, editAnimalDetailsAnimalRemarks, editAnimalDetailsAnimalAge ;
    private Spinner editAnimalDetailsAnimalTypeSpinner, editAnimalDetailsAnimalStatusSpinner ;
    private Button editAnimalDetailsSubmitButton, editAnimalDetailsCancelButton, editAnimalDetailsDeleteButton, editAnimalDetailsEntryDateButton, editAnimalDetailsCompletionDateButton ;
    private LinearLayout editAnimalDetailsEmptyDataLayout ;

    private TextView editAnimalDetailsEntryDateTextView, editAnimalDetailsCompletionDateTextView ;

    //For date
    private MaterialDatePicker.Builder materialEntryDateBuilder, materialCompletionDateBuilder ;
    private MaterialDatePicker materialEntryDatePicker, materialCompletionDatePicker ;

    //Adapters for spinners
    ArrayAdapter<String> arrayAdapterForAnimalType ;
    ArrayAdapter<String> arrayAdapterForAnimalStatus ;

    //Animal Case Id for updation and deletion
    String ANIMAL_CASE_ID ;

    //Animal Name
    String ANIMAL_NAME ;

    //For database object
    AnimalDatabaseHelper dbHelper ;

    //Top level layout for add animals
    ConstraintLayout editAnimalDetailsConstraintLayout ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_animal_details);

        //Initializing the database
        dbHelper = new AnimalDatabaseHelper(this) ;

        //Top level layout
        editAnimalDetailsConstraintLayout = findViewById(R.id.editAnimalDetailsConstraintLayout) ;

        //Setting spinner values
        arrayAdapterForAnimalType = new ArrayAdapter<String>(this,
                R.layout.spinner_layout,animalTypeSpinnerData) ;
        arrayAdapterForAnimalType.setDropDownViewResource( R.layout.spinner_layou_dropdown);
        editAnimalDetailsAnimalTypeSpinner = findViewById(R.id.editAnimalDetailsAnimalTypeSpinner) ;
        editAnimalDetailsAnimalTypeSpinner.setAdapter(arrayAdapterForAnimalType);

        arrayAdapterForAnimalStatus = new ArrayAdapter<String>(this,
                R.layout.spinner_layout,animalStatusSpinnerData) ;
        arrayAdapterForAnimalStatus.setDropDownViewResource( R.layout.spinner_layou_dropdown);
        editAnimalDetailsAnimalStatusSpinner = findViewById(R.id.editAnimalDetailsAnimalStatusSpinner) ;
        editAnimalDetailsAnimalStatusSpinner.setAdapter(arrayAdapterForAnimalStatus);

        editAnimalDetailsImageView = findViewById(R.id.editAnimalDetailsImageView) ;
        editAnimalDetailsAddImageFloatingBtn = findViewById(R.id.editAnimalDetailsAddImageFloatingBtn) ;
        editAnimalDetailsAnimalName = findViewById(R.id.editAnimalDetailsAnimalName) ;
        editAnimalDetailsAnimalDisease = findViewById(R.id.editAnimalDetailsAnimalDisease) ;
        editAnimalDetailsAnimalLocation = findViewById(R.id.editAnimalDetailsAnimalLocation) ;
        editAnimalDetailsAnimalReporter = findViewById(R.id.editAnimalDetailsAnimalReporter) ;
        editAnimalDetailsAnimalReporterContact = findViewById(R.id.editAnimalDetailsAnimalReporterContact) ;
        editAnimalDetailsAnimalRemarks = findViewById(R.id.editAnimalDetailsAnimalRemarks) ;
        editAnimalDetailsEntryDateTextView = findViewById(R.id.editAnimalDetailsEntryDateTextView) ;
        editAnimalDetailsCompletionDateTextView = findViewById(R.id.editAnimalDetailsCompletionDateTextView) ;
        editAnimalDetailsAnimalAge = findViewById(R.id.editAnimalDetailsAnimalAge) ;

        //Buttons
        editAnimalDetailsSubmitButton = findViewById(R.id.editAnimalDetailsSubmitButton) ;
        editAnimalDetailsCancelButton = findViewById(R.id.editAnimalDetailsCancelButton) ;
        editAnimalDetailsDeleteButton = findViewById(R.id.editAnimalDetailsDeleteButton) ;
        editAnimalDetailsEntryDateButton = findViewById(R.id.editAnimalDetailsEntryDateButton) ;
        editAnimalDetailsCompletionDateButton = findViewById(R.id.editAnimalDetailsCompletionDateButton) ;

        //layout if no image is selected
        editAnimalDetailsEmptyDataLayout = findViewById(R.id.editAnimalDetailsEmptyDataLayout) ;

        //Getting dat by intent
        getAndSetIntentData() ;

        //Setting Title of Action bar
        ActionBar actionBar = getSupportActionBar() ;
        actionBar.setTitle(ANIMAL_NAME);

        //Disabling completion button
        editAnimalDetailsCompletionDateButton.setEnabled(false);

        //Getting datePicker
        materialEntryDateBuilder = Utils.datePicker("Select Entry Date");
        materialCompletionDateBuilder = Utils.datePicker("Select Completion Date");
        materialEntryDatePicker = materialEntryDateBuilder.build() ;

        editAnimalDetailsEntryDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialEntryDatePicker.addOnPositiveButtonClickListener(
                                new MaterialPickerOnPositiveButtonClickListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onPositiveButtonClick(Object selection) {
                                        editAnimalDetailsCompletionDateTextView.setText("Completion Date");
                                        editAnimalDetailsCompletionDateButton.setEnabled(true);
                                        editAnimalDetailsEntryDateTextView.setText(materialEntryDatePicker.getHeaderText());
                                    }
                                });
                        materialEntryDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });


        editAnimalDetailsCompletionDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!editAnimalDetailsEntryDateTextView.getText().toString().equals("Entry Date")){
                            long entryDateMilliSeconds = Long.parseLong(Utils.getMilliSecondDate(editAnimalDetailsEntryDateTextView.getText().toString().trim()));
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
                                        editAnimalDetailsCompletionDateTextView.setText(materialCompletionDatePicker.getHeaderText());
                                    }
                                });
                        materialCompletionDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        editAnimalDetailsAddImageFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.hasStoragePermission(EditAnimalDetails.this)){
                    Utils.openImageChooser(EditAnimalDetails.this) ;
                }
                else{
                    ActivityCompat.requestPermissions(((AppCompatActivity) EditAnimalDetails.this), new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 200);
                }
            }
        });

        editAnimalDetailsCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditAnimalDetails.this);

                builder.setTitle("WARNING, Entries are not saved!");
                builder.setMessage("Are you sure to cancel the operation?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Backed successfully
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
        });

        editAnimalDetailsDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    dbHelper.open() ;
                    boolean isDataDeleted = dbHelper.deleteAnimalData(ANIMAL_CASE_ID) ;
                    if(isDataDeleted){
                        Utils.showToast(EditAnimalDetails.this, "Entry deleted successfully");
                        setResult(RESULT_OK);
                        finish();
                    }
                    else{
                        Utils.showToast(EditAnimalDetails.this,"Entry is not deleted");
                    }
                    dbHelper.close();
                }
                catch (Exception e){
                    Utils.showToast(EditAnimalDetails.this,"Error!, entry can not be deleted");
                    dbHelper.close();
                }
            }
        });
        
        editAnimalDetailsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting image from imageView
                editAnimalDetailsImageView.buildDrawingCache();
                Bitmap bitmap = editAnimalDetailsImageView.getDrawingCache();
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream() ;
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteBuffer);

                //Getting details from ETLayout
                String animalNameTxt = Utils.getTextFromETLayout(editAnimalDetailsAnimalName) ;
                String animalDiseaseTxt = Utils.getTextFromETLayout(editAnimalDetailsAnimalDisease) ;
                String animalLocationTxt = Utils.getTextFromETLayout(editAnimalDetailsAnimalLocation) ;
                String animalReporterTxt = Utils.getTextFromETLayout(editAnimalDetailsAnimalReporter) ;
                String animalReporterContactTxt = Utils.getTextFromETLayout(editAnimalDetailsAnimalReporterContact) ;
                String animalRemarksTxt = Utils.getTextFromETLayout(editAnimalDetailsAnimalRemarks) ;
                String animalAgeTxt = Utils.getTextFromETLayout(editAnimalDetailsAnimalAge) ;

                //Getting details from Spinners
                String animalTypeTxt = Utils.getValueFromSpinner(editAnimalDetailsAnimalTypeSpinner) ;
                String animalStatusTxt = Utils.getValueFromSpinner(editAnimalDetailsAnimalStatusSpinner) ;

                //Getting dates
                String animalEntryDateTxt = Utils.getTextTextView(editAnimalDetailsEntryDateTextView) ;
                String animalEntryDateInMilliSeconds ;
                if(animalEntryDateTxt.equals("Entry Date"))
                    animalEntryDateInMilliSeconds = "";
                else
                    animalEntryDateInMilliSeconds = Utils.getMilliSecondDate(animalEntryDateTxt) ;
                String animalCompletionDateTxt = Utils.getTextTextView(editAnimalDetailsCompletionDateTextView) ;
                String animalCompletionDateInMilliSeconds ;
                if(animalCompletionDateTxt.equals("Completion Date"))
                    animalCompletionDateInMilliSeconds = "";
                else
                    animalCompletionDateInMilliSeconds = Utils.getMilliSecondDate(animalCompletionDateTxt) ;
                try{
                    dbHelper.open() ;
                    byte [] animalImageBytes = byteBuffer.toByteArray();
                    Log.i("Data:", "Data to be updated:- animalName:" + animalNameTxt + ", animalType:" + animalTypeTxt + ", animalStatus:" + animalStatusTxt
                            + ", animalDisease:" + animalDiseaseTxt + ", animalLocation:" + animalLocationTxt + ", animalRemarks:" + animalRemarksTxt + ", animalReporter:" + animalReporterTxt + ", animalReporterContact:" +
                            animalReporterContactTxt + ", animalRemarks:" + animalRemarksTxt + ", \n animalImageByteArray:" + animalImageBytes + ",\n" + ", animalEntryDate: (" + animalEntryDateTxt + ")" + animalEntryDateInMilliSeconds +
                            ", animalCompletionDate: (" + animalCompletionDateTxt + ")"+ animalCompletionDateInMilliSeconds + ", animalAge:" + animalAgeTxt
                    );
                    boolean isDataUpdated = dbHelper.updateAnimalData(ANIMAL_CASE_ID,animalNameTxt,animalTypeTxt,animalStatusTxt,
                            animalDiseaseTxt,animalLocationTxt,animalReporterTxt,
                            animalReporterContactTxt,animalRemarksTxt,animalImageBytes, animalEntryDateInMilliSeconds, animalCompletionDateInMilliSeconds, animalAgeTxt) ;
                    if(isDataUpdated){
                        Utils.showToast(EditAnimalDetails.this, "Entry updated successfully");
                        setResult(RESULT_OK);
                        finish();
                    }
                    else{
                        Utils.showToast(EditAnimalDetails.this,"Entry is not updated");
                    }
                    dbHelper.close();
                }
                catch (Exception e){
                    Utils.showToast(EditAnimalDetails.this,"Error!, Entry can not be updated");
                    dbHelper.close();
                }
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
                EditAnimalDetails.super.onBackPressed();

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
                    editAnimalDetailsEmptyDataLayout.setVisibility(View.GONE);
                    editAnimalDetailsImageView.setImageURI(selectImageUri);
                }
            }
        }
    }

    void getAndSetIntentData() {
        if(getIntent().hasExtra("CASE_ID") ){
            ANIMAL_CASE_ID = getIntent().getStringExtra("CASE_ID") ;
            ANIMAL_NAME = getIntent().getStringExtra("ANIMAL_NAME") ;
            dbHelper.open() ;
            Cursor res = dbHelper.getSingleAnimalDetails(ANIMAL_CASE_ID) ;
            if(res.getCount() == 0){
                Utils.showToast(EditAnimalDetails.this, "No animal record found");
                if(editAnimalDetailsImageView.getDrawable() == null){
                        editAnimalDetailsEmptyDataLayout.setVisibility(View.VISIBLE);
                    }
            }
            else{

                while(res.moveToNext()) {

                    editAnimalDetailsAnimalName.setText(res.getString(1));
                    editAnimalDetailsAnimalTypeSpinner.setSelection(arrayAdapterForAnimalType.getPosition(res.getString(2)));
                    editAnimalDetailsAnimalStatusSpinner.setSelection(arrayAdapterForAnimalStatus.getPosition(res.getString(3)));
                    editAnimalDetailsAnimalDisease.setText(res.getString(4));
                    editAnimalDetailsAnimalLocation.setText(res.getString(5));
                    editAnimalDetailsAnimalReporter.setText(res.getString(6));
                    editAnimalDetailsAnimalReporterContact.setText(res.getString(7));
                    editAnimalDetailsAnimalRemarks.setText(res.getString(8));
                    //Parsing and storing image
                    @SuppressLint("Range") byte[] blob = res.getBlob(9) ;

                    editAnimalDetailsImageView.setImageBitmap((Bitmap)Utils.getImage(blob));
                    String entryDateString = Utils.getStringDate(res.getString(10));
                    if(!entryDateString.isEmpty())
                      editAnimalDetailsEntryDateTextView.setText(entryDateString);
                    String completionDateString = Utils.getStringDate(res.getString(11));
                    if(!completionDateString.isEmpty())
                        editAnimalDetailsCompletionDateTextView.setText(completionDateString);
                    String animalAgeString = res.getString(12);
                    if(!animalAgeString.isEmpty())
                        editAnimalDetailsAnimalAge.setText(animalAgeString);
                }
            }
            dbHelper.close();
        }
        else{
            Toast.makeText(this, "No data sent", Toast.LENGTH_SHORT).show();
        }
    }
}