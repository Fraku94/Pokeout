package com.example.pokeout.pokeout.UserDescryption;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;

import java.util.List;

/**
 * Created by Z710 on 2018-02-27.
 */

public class UserDescryptionAdapter extends RecyclerView.Adapter<UserDescryptionViewHolder>{

    private List<UserDescryptionObject> userDescryptionObjectsList;

    private Context context;

    //Przypisanie Obiektów do adaptera
    public UserDescryptionAdapter(List<UserDescryptionObject> userDescryptionObjectsList, Context context){
        this.userDescryptionObjectsList = userDescryptionObjectsList;
        this.context = context;
    }

    //Tworzenie wyglądu i przypisanie ViewHoldera do adaptera
    @Override
    public UserDescryptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        //Przypisanie wygladu okna do adaptera
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_descryption, null,false);

        //Ustawienie RecycleView
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        //Podpienie i zwrocenie wygladu
        UserDescryptionViewHolder rcv = new UserDescryptionViewHolder((layoutView));

        return rcv;
    }
    //Nadanie wartosci do okienek, pobranie ich z Objektow
    @Override
    public void onBindViewHolder(final UserDescryptionViewHolder holder, final int position) {

        //Ustawienie tekstu dla imienia
        holder.mUserDescryptionName.setText(userDescryptionObjectsList.get(position).getName());

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView
        if(!userDescryptionObjectsList.get(position).getImageUrl().equals("default")){
            Glide.with(context).load(userDescryptionObjectsList.get(position).getImageUrl()).into(holder.mUserDescryptionImage);
        }
    }

    //Liczba prawdobodobnie ilości tych okien do załadowania
    @Override
    public int getItemCount() {
        return this.userDescryptionObjectsList.size();
    }
}