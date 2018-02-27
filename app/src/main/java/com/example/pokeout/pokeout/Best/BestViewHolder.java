package com.example.pokeout.pokeout.Best;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-02-27.
 */

public class BestViewHolder extends RecyclerView.ViewHolder  {
    public TextView mBestName, mBestCount;
    public ImageView mBestImage, mBestGo, mBestFollow;


    public BestViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mBestCount = (TextView) itemView.findViewById(R.id.bestCount);
        mBestName = (TextView) itemView.findViewById(R.id.bestName);
        mBestImage = (ImageView) itemView.findViewById(R.id.bestImage);
        mBestGo = (ImageView) itemView.findViewById(R.id.bestGo);
        mBestFollow = (ImageView) itemView.findViewById(R.id.bestFollow);


    }

}