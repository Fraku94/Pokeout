package com.example.pokeout.pokeout.Liked;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;

import java.util.List;

/**
 * Created by Z710 on 2018-02-22.
 */

public class LikedAdapter extends RecyclerView.Adapter<LikedViewHolder> {

    private List<LikedObject> likedList;
    private Context context;

    //Przypisanie Obiektów do adaptera
    public LikedAdapter(List<LikedObject> likedList, Context context){
        this.likedList = likedList;
        this.context = context;
    }

    //Tworzenie wyglądu i przypisanie ViewHoldera do adaptera
    @Override
    public LikedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        //Przypisanie wygladu okna do adaptera
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liked, null,false);

        //Ustawienie RecycleView
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        //Podpienie i zwrocenie wygladu
        LikedViewHolder rcv = new LikedViewHolder((layoutView));

        return rcv;
    }

    //Nadanie wartosci do okienek, pobranie ich z Objektow
    @Override
    public void onBindViewHolder(LikedViewHolder holder, int position) {

        holder.mCategoryId = likedList.get(position).getCategoryId();
        holder.mCategoryName.setText(likedList.get(position).getName());
        holder.mCategoryCount.setText(likedList.get(position).getCount());

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView
        if(!likedList.get(position).getCategoryImageUrl().equals("default")){
            Glide.with(context).load(likedList.get(position).getCategoryImageUrl()).into(holder.mCategoryImage);
        }



    }

    //Liczba prawdobodobnie ilości tych okien do załadowania
    @Override
    public int getItemCount() {
        return this.likedList.size();
    }
}
