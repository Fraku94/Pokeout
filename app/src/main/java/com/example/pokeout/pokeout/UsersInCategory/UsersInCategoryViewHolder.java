package com.example.pokeout.pokeout.UsersInCategory;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-03-16.
 */

public class UsersInCategoryViewHolder extends RecyclerView.ViewHolder{
    public TextView mUserInCatName,mCityUser,mUserDistance;
    public ImageView mUserInCatImage;
    public ImageView mUserInCatYes,mUserInCatNo;



    public UsersInCategoryViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mUserInCatName = itemView.findViewById(R.id.usersInCategoryName);
        mUserInCatImage = itemView.findViewById(R.id.usersInCategoryImage);
        mUserInCatYes = itemView.findViewById(R.id.usersInCategoryYes);
        mUserInCatNo = itemView.findViewById(R.id.usersInCategoryNo);
        mCityUser = itemView.findViewById(R.id.cityUser);
        mUserDistance = itemView.findViewById(R.id.UserDistance);

//        mUsersInCategoryMessage = (ImageView) itemView.findViewById(R.id.usersInCategoryMessage);

    }


}