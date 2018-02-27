package com.example.pokeout.pokeout.UsersInCategory;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-02-26.
 */

public class UsersInCategoryViewHolder extends RecyclerView.ViewHolder{
    public TextView mUserInCatName;
    public ImageView mUserInCatImage;
    public ImageView mUserInCatBFollow, mUsersInCategoryMessage;

    public UsersInCategoryViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mUserInCatName = (TextView) itemView.findViewById(R.id.usersInCategoryName);
        mUserInCatImage = (ImageView) itemView.findViewById(R.id.usersInCategoryImage);
        mUserInCatBFollow = (ImageView) itemView.findViewById(R.id.usersInCategoryfollow);
        mUsersInCategoryMessage = (ImageView) itemView.findViewById(R.id.usersInCategoryMessage);

    }


}