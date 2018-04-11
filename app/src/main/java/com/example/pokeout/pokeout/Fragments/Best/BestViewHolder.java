package com.example.pokeout.pokeout.Fragments.Best;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;
import com.jackandphantom.androidlikebutton.AndroidLikeButton;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkButtonBuilder;

/**
 * Created by Z710 on 2018-02-27.
 */

public class BestViewHolder extends RecyclerView.ViewHolder  {
    public TextView mBestName, mBestCount;
    public ImageView mBestImage;
//public SparkButton mBestFollow;
    public RelativeLayout mGointo;
    public ShineButton mBestFollow;



    public BestViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mBestCount = itemView.findViewById(R.id.bestCount);
        mBestName = itemView.findViewById(R.id.bestName);
        mBestImage = itemView.findViewById(R.id.bestImage);
        mBestFollow = itemView.findViewById(R.id.bestFollow);


        mGointo = itemView.findViewById(R.id.Gointo);


    }

}