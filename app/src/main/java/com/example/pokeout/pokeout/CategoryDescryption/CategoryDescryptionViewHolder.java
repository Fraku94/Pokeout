package com.example.pokeout.pokeout.CategoryDescryption;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-02-27.
 */

public class CategoryDescryptionViewHolder extends RecyclerView.ViewHolder  {
    public TextView mCategoryDescryptionName;
    public ImageView mCategoryDescryptionImage;


    public CategoryDescryptionViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mCategoryDescryptionName = itemView.findViewById(R.id.categoryDescryptionName);
        mCategoryDescryptionImage = itemView.findViewById(R.id.categoryDescryptionImage);



    }

}