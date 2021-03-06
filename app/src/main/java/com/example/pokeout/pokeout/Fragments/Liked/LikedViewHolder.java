package com.example.pokeout.pokeout.Fragments.Liked;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;
import com.sackcentury.shinebuttonlib.ShineButton;


public class LikedViewHolder extends RecyclerView.ViewHolder {
    public TextView mCategoryName, mCategoryCount;
    public ImageView mCategoryImage;
    public String mCategoryId;
    public RelativeLayout mGointo;
    public ShineButton mLikedFollow;


    public LikedViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mCategoryCount = itemView.findViewById(R.id.categoryCount);
        mCategoryName = itemView.findViewById(R.id.categoryName);
        mCategoryImage = itemView.findViewById(R.id.categoryImage);
        mGointo = itemView.findViewById(R.id.Gointo);

        mLikedFollow = itemView.findViewById(R.id.likedFollow);
    }


}
