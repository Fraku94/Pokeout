package com.example.pokeout.pokeout.Fragments.Suggest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-02-27.
 */

public class SuggestViewHolder extends RecyclerView.ViewHolder  {
    public TextView mSuggestName, mSuggestCount;
    public ImageView mSuggestImage, mSuggestGo, mSuggestFollow;


    public SuggestViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mSuggestCount = (TextView) itemView.findViewById(R.id.suggestCount);
        mSuggestName = (TextView) itemView.findViewById(R.id.suggestName);
        mSuggestImage = (ImageView) itemView.findViewById(R.id.suggestImage);
        mSuggestGo = (ImageView) itemView.findViewById(R.id.suggestGo);
        mSuggestFollow = (ImageView) itemView.findViewById(R.id.suggestFollow);


    }

}