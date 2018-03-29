package com.example.pokeout.pokeout.UserInCategory;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-02-26.
 */

public class UserInCategoryViewHolder extends RecyclerView.ViewHolder{
    public TextView name, description, price;
    //        public ImageView thumbnail;
    public RelativeLayout viewLeft, viewForeground;
    public UserInCategoryViewHolder(View itemView) {
        super(itemView);

        //Tu wpisujesz wszystkie TextView,ImageView,itp jakie dodajesz w item_....xml (tutaj item_liked.xml)
        name = itemView.findViewById(R.id.name);
        description = itemView.findViewById(R.id.description);
        price = itemView.findViewById(R.id.price);
//            thumbnail = view.findViewById(R.id.thumbnail);
        viewLeft = itemView.findViewById(R.id.view_left);
        viewForeground = itemView.findViewById(R.id.view_foreground);

    }


}