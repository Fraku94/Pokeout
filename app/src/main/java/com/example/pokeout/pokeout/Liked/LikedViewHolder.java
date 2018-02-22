package com.example.pokeout.pokeout.Liked;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-02-22.
 */

public class LikedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mCategoryId,mCategoryName;
    public ImageView mCategoryImage;

    public LikedViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        mCategoryId = (TextView) itemView.findViewById(R.id.categoryId);
        mCategoryName = (TextView) itemView.findViewById(R.id.categoryName);
        mCategoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
    }

    @Override
    public void onClick(View v) {

    }
}
