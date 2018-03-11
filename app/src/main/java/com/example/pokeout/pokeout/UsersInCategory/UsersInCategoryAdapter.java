package com.example.pokeout.pokeout.UsersInCategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;


import java.util.List;

/**
 * Created by Z710 on 2018-02-26.
 */

public class UsersInCategoryAdapter  extends ArrayAdapter<UsersInCategoryObject> {

    Context context;

    public UsersInCategoryAdapter(Context context, int resourceId, List<UsersInCategoryObject> items){
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        UsersInCategoryObject card_item = getItem(position);

        if (convertView == null){
            //Przypisanie pliku xml item
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_users_in_category, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);




        name.setText(card_item.getName());
        //Pobranie zdjecia i zapisanie go w imageView
        switch(card_item.getImageUrl()){
            case "default":
                //domyslne
                Glide.with(convertView.getContext()).load(R.mipmap.ic_default).into(image);
                break;
            default:
                //z Firebase
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(card_item.getImageUrl()).into(image);
                break;
        }



        return convertView;

    }
}
