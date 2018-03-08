package com.example.pokeout.pokeout.Fragments.Liked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UsersInCategory.UsersInCategoryActivity;

/**
 * Created by Z710 on 2018-02-22.
 */

public class LikedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mCategoryName, mCategoryCount;
    public ImageView mCategoryImage;
    public String mCategoryId;

    public LikedViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mCategoryCount = (TextView) itemView.findViewById(R.id.categoryCount);
        mCategoryName = (TextView) itemView.findViewById(R.id.categoryName);
        mCategoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);

    }

    //Tutaj masz co sie stanie gdy sobie klikniesz w dana kategorie (prawdopodobnie :D)
    @Override
    public void onClick(View v) {

        //Przekazanie id kategorii
        //Klikniecie w ikone idz do uzytkownikow z kategporii
        Intent intent = new Intent(v.getContext(), UsersInCategoryActivity.class);
        Bundle b = new Bundle();
        b.putString("CategoryId", mCategoryId);
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }


}
