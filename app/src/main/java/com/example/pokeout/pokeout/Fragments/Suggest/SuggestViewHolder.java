package com.example.pokeout.pokeout.Fragments.Suggest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.varunest.sparkbutton.SparkButton;

/**
 * Created by Z710 on 2018-02-27.
 */

public class SuggestViewHolder extends RecyclerView.ViewHolder  {
    public TextView mSuggestName, mSuggestCount;
    public ImageView mSuggestImage, mSuggestGo;
    public RelativeLayout mGointo;
    public  ShineButton mSuggestFollow;
//public SparkButton mSuggestFollow;
    public SuggestViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mSuggestCount = itemView.findViewById(R.id.suggestCount);
        mSuggestName = itemView.findViewById(R.id.suggestName);
        mSuggestImage = itemView.findViewById(R.id.suggestImage);
//        mSuggestGo = (ImageView) itemView.findViewById(R.id.suggestGo);
        mSuggestFollow = itemView.findViewById(R.id.suggestFollow);
//        mSuggestUnfollow = (ShineButton) itemView.findViewById(R.id.suggestFollow);

        mGointo = itemView.findViewById(R.id.Gointo);
    }

}