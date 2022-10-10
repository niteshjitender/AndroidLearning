package com.world.love.animalX;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    //Image Request code
    public static final int  SELECT_PICTURE = 100 ;
    public static final int EDIT_DETAILS = 101;

    final static String dateFormat = "MMM dd, yyyy" ;

    public static Bitmap getImage(byte []  image ){
        return BitmapFactory.decodeByteArray(image,0, image.length) ;
    }


    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream() ;
        int bufferSize = 1024*15 ;
        byte [] buffer = new byte[bufferSize] ;
        int len = 0 ;
        while((len = inputStream.read(buffer)) != -1){
            byteBuffer.write(buffer, 0 , len);
        }
        return byteBuffer.toByteArray() ;
    }

    //For permissions
    public static boolean hasStoragePermission(Context context) {
        int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) ;
        return read == PackageManager.PERMISSION_GRANTED ;
    }

    /** 
    This function starts the activity to choose the image file
    @param AppCompatActivity context
    */
    public static void openImageChooser(AppCompatActivity context) {
        //Using the library
        ImagePicker.with(context)
                .crop(9.62f,5.83f)	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(SELECT_PICTURE) ;

    }

    /**
    This method showMessage in snackbar
    @param ViewGroup layout
    @param String message that needs to be shown
     */
    public static void showMessage(ViewGroup layout, String message){
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG) ;
        snackbar.show();
    }

    public static void showToast(AppCompatActivity context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getTextFromETLayout(EditText txt){
        return txt.getText().toString().trim() ;
    }

    public static String getTextTextView(TextView txt){
        return txt.getText().toString().trim() ;
    }



    public static String getValueFromSpinner(Spinner spn){
        return spn.getSelectedItem().toString().trim() ;
    }

    //String Date to milliseconds String
    public static String getMilliSecondDate(String toParse){
        if(toParse.equals(""))
            return "" ;
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(toParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return String.valueOf(millis) ;
    }

    //Millisecond string date to simple date
    /** @param milliSeconds
     * */
    public static String getStringDate(String milliSeconds)
    {
        if(milliSeconds.equals(""))
            return "" ;
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        String date =  formatter.format(calendar.getTime());
        return date ;
    }

    //For picking up date
    public static MaterialDatePicker.Builder datePicker(String title){
        // now create instance of the material date picker
        // builder make sure to add the "datePicker" which
        // is normal material date picker which is the first
        // type of the date picker in material design date
        // picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText(title);

        // now create the instance of the material date
        // picker
        return  materialDateBuilder;
    }
}
