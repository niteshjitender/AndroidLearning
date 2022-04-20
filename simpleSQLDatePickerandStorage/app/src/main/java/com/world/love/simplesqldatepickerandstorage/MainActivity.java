package com.world.love.simplesqldatepickerandstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.world.love.simplesqldatepickerandstorage.Utils.DateUtils;

public class MainActivity extends AppCompatActivity {

    private Button mPickDateButton;

    private TextView mShowSelectedDateText, show_selected_date_in_milliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // now register the text view and the button with
        // their appropriate IDs
        mPickDateButton = findViewById(R.id.pick_date_button);
        mShowSelectedDateText = findViewById(R.id.show_selected_date);
        show_selected_date_in_milliseconds = findViewById(R.id.show_selected_date_in_milliseconds) ;


        //Getting datePicker
        final MaterialDatePicker materialDatePicker = DateUtils.datePicker("Select The Date");

        // handle select date button which opens the
        // material design date picker
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
//                        mShowSelectedDateText.setText("Selected Date is : " + materialDatePicker.getHeaderText());
                        show_selected_date_in_milliseconds.setText(DateUtils.getMilliSecondDate(materialDatePicker.getHeaderText()));
                        mShowSelectedDateText.setText("Selected Date is : " + DateUtils.getStringDate((String)show_selected_date_in_milliseconds.getText()));

                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });
    }
}