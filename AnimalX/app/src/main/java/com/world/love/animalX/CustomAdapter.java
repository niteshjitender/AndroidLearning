package com.world.love.animalX;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    //For context
    Context context ;
    //For position of item in array
    int position ;
    //For activity
    Activity activity ;
    //For animation in cards
    Animation translate_anim ;

    //Arrays for adapters
    ArrayList animalCardImageViewArray, animalCardCaseIDArray, animalCardNameArray, animalCardTypeArray, animalCardStatusBarArray, animalCardShowStatusArray;

    public CustomAdapter(Activity activity, Context context, ArrayList animalCardImageView,  ArrayList animalCardCaseID, ArrayList animalCardNameArray,
                         ArrayList animalCardType, ArrayList animalCardStatusBar, ArrayList animalCardShowStatus) {
        this.activity = activity ;
        this.context = context;
        this.animalCardImageViewArray = animalCardImageView ;
        this.animalCardCaseIDArray = animalCardCaseID ;
        this.animalCardNameArray = animalCardNameArray;
        this.animalCardTypeArray = animalCardType ;
        this.animalCardStatusBarArray = animalCardStatusBar ;
        this.animalCardShowStatusArray = animalCardShowStatus ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context) ;
        View view = inflater.inflate(R.layout.animal_card, parent, false) ;
        return new MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {

          holder.animalCardCaseID.setText(String.valueOf(animalCardCaseIDArray.get(position)));
          holder.animalCardName.setText(String.valueOf(animalCardNameArray.get(position)));
          holder.animalCardType.setText(String.valueOf(animalCardTypeArray.get(position)));
          holder.animalCardShowStatus.setText(String.valueOf(animalCardShowStatusArray.get(position)));

          //Setting color for status bar
          String colorChosenForStatus = String.valueOf(animalCardStatusBarArray.get(position));
          switch (colorChosenForStatus){
              case "IN_PROCESS" : holder.animalCardStatusBar.setBackgroundColor(Color.parseColor("yellow")); break ;
              case "RELEASED" : holder.animalCardStatusBar.setBackgroundColor(Color.parseColor("#11FF00")); break ;
              default: holder.animalCardStatusBar.setBackgroundColor(Color.parseColor("red"));
          }

          //Setting animal Image
          holder.animalCardImageView.setImageBitmap((Bitmap)animalCardImageViewArray.get(position)) ;

          //Recycler view on card press
          holder.animalCardTopLevelConstraintLayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(context, EditAnimalDetails.class) ;
                  intent.putExtra("CASE_ID", String.valueOf(animalCardCaseIDArray.get(position))) ;
                  intent.putExtra("ANIMAL_NAME", holder.animalCardName.getText().toString().trim()) ;
                  activity.startActivityForResult(intent, Utils.EDIT_DETAILS);
              }
          });
    }

    @Override
    public int getItemCount() {
        return animalCardCaseIDArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //AnimalCard top level layout
        ConstraintLayout animalCardTopLevelConstraintLayout;

        TextView animalCardCaseID, animalCardName, animalCardType, animalCardShowStatus ;
        ImageView animalCardImageView ;
        LinearLayout animalCardStatusBar ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //Setting animation to top level view in cards
            animalCardTopLevelConstraintLayout = itemView.findViewById(R.id.animalCardTopLevelConstraintLayout) ;
            translate_anim = AnimationUtils.loadAnimation(context,R.anim.translate_anim);
            animalCardTopLevelConstraintLayout.setAnimation(translate_anim);

            animalCardCaseID = itemView.findViewById(R.id.animalCardCaseID) ;
            animalCardName = itemView.findViewById(R.id.animalCardName) ;
            animalCardType = itemView.findViewById(R.id.animalCardType) ;
            animalCardShowStatus = itemView.findViewById(R.id.animalCardShowStatus) ;
            animalCardImageView = itemView.findViewById(R.id.animalCardImageCardImageView) ;
            animalCardStatusBar = itemView.findViewById(R.id.animalCardStatusBar) ;
        }

    }


}