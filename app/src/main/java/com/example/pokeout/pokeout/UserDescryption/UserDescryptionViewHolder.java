package com.example.pokeout.pokeout.UserDescryption;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-02-27.
 */

public class UserDescryptionViewHolder extends RecyclerView.ViewHolder  {
    public TextView mUserDescryptionName;
    public ImageView mUserDescryptionImage;
    public RelativeLayout mbestGointo;

    public UserDescryptionViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        mUserDescryptionName = (TextView) itemView.findViewById(R.id.userDescryptionName);
        mUserDescryptionImage = (ImageView) itemView.findViewById(R.id.userDescryptionImage);
        mbestGointo = (RelativeLayout) itemView.findViewById(R.id.bestGointo);


    }

}