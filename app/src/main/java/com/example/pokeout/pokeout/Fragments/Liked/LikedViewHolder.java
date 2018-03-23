package com.example.pokeout.pokeout.Fragments.Liked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UserInCategory.UserInCategoryActivity;
import com.example.pokeout.pokeout.UsersInCategory.UsersInCategoryActivity;

/**
 * Created by Z710 on 2018-02-22.
 */

public class LikedViewHolder extends RecyclerView.ViewHolder {
    public TextView mCategoryName, mCategoryCount;
    public ImageView mCategoryImage, mLikedFollow;
    public String mCategoryId;
    public RelativeLayout mGointo;


    public LikedViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mCategoryCount = (TextView) itemView.findViewById(R.id.categoryCount);
        mCategoryName = (TextView) itemView.findViewById(R.id.categoryName);
        mCategoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
        mGointo = (RelativeLayout) itemView.findViewById(R.id.Gointo);

        mLikedFollow = (ImageView) itemView.findViewById(R.id.likedFollow);
    }


}
