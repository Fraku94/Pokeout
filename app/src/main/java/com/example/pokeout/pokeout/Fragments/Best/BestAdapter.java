package com.example.pokeout.pokeout.Fragments.Best;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.CategoryDescryption.CategoryDescryptionActivity;
import com.example.pokeout.pokeout.CategoryInformation;
import com.example.pokeout.pokeout.Fragments.Best.BestObject;
import com.example.pokeout.pokeout.Fragments.Best.BestViewHolder;
import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UsersInCategory.UsersInCategoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Z710 on 2018-02-27.
 */

public class BestAdapter extends RecyclerView.Adapter<BestViewHolder>{

    private List<BestObject> bestObjectsList;

    private Context context;

    //Przypisanie Obiektów do adaptera
    public BestAdapter(List<BestObject> bestObjectsList, Context context){
        this.bestObjectsList = bestObjectsList;
        this.context = context;
    }

    //Tworzenie wyglądu i przypisanie ViewHoldera do adaptera
    @Override
    public BestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        //Przypisanie wygladu okna do adaptera
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best, null,false);

        //Ustawienie RecycleView
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        //Podpienie i zwrocenie wygladu
        BestViewHolder rcv = new BestViewHolder((layoutView));

        return rcv;
    }
    //Nadanie wartosci do okienek, pobranie ich z Objektow
    @Override
    public void onBindViewHolder(final BestViewHolder holder, final int position) {

        //Ustawienie tekstu dla imienia
        holder.mBestName.setText(bestObjectsList.get(position).getName());

        //Ustawienie tekstu dla wartosci liczby obserwujacych uzytkownikow
        holder.mBestCount.setText(bestObjectsList.get(position).getCount());

        //Sprawdzenie i ustawienie czy kategorie mamy juz dodana czy nie (odpowiednia zmiana ikon)
        if(CategoryInformation.listFollowingCategory.contains(bestObjectsList.get(position).getId())){
            holder.mBestFollow.setImageResource(R.drawable.like2);
            holder.mbestGointo.setVisibility(View.VISIBLE);
        }else if (!CategoryInformation.listFollowingCategory.contains(bestObjectsList.get(position).getId())){
            holder.mBestFollow.setImageResource(R.drawable.unffalow);
            holder.mbestGointo.setVisibility(View.INVISIBLE);
        }

        //Klikniecie w ikone dodawania kategori do obserwowanych
        holder.mBestFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Przypisanie ID obecnego uzytkwonika
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                //Jesli w "categoryInformation" nie ma id kategorii to ma ja doda i zmienic odpowiednio ikony
                if(!CategoryInformation.listFollowingCategory.contains(bestObjectsList.get(position).getId())){
                    holder.mBestFollow.setImageResource(R.drawable.like2);
                    holder.mbestGointo.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("category").child(bestObjectsList.get(position).getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Category").child(bestObjectsList.get(position).getId()).child("users").child(userId).setValue(true);
                }

                //Jesli w "categoryInformation" jest id kategorii to ma ja usunac i zmienic odpowiednio ikony
                else{
                    holder.mBestFollow.setImageResource(R.drawable.unffalow);
                    holder.mbestGointo.setVisibility(View.INVISIBLE);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("category").child(bestObjectsList.get(position).getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Category").child(bestObjectsList.get(position).getId()).child("users").child(userId).removeValue();
                }
            }
        });

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView
        if(!bestObjectsList.get(position).getImageUrl().equals("default")){
            Glide.with(context).load(bestObjectsList.get(position).getImageUrl()).into(holder.mBestImage);
        }

        //Klikniecie w ikone idz do szczegolow kategorii
        holder.mBestImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Przekazanie danych potzrebnych do opisania szczegolow kategorii
                Intent intent = new Intent(v.getContext(), CategoryDescryptionActivity.class);
                Bundle b = new Bundle();
                b.putString("Id", bestObjectsList.get(position).getId());
                b.putString("Name", bestObjectsList.get(position).getName());
                b.putString("ImageUrl", bestObjectsList.get(position).getImageUrl());
                b.putString("Descryption", bestObjectsList.get(position).getCatDescryption());
                intent.putExtras(b);
                v.getContext().startActivity(intent);

            }
        });

        //Klikniecie w ikone idz do uzytkownikow z kategporii
        holder.mbestGointo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), UsersInCategoryActivity.class);
                Bundle b = new Bundle();
                b.putString("CategoryId", bestObjectsList.get(position).getId());
                intent.putExtras(b);
                v.getContext().startActivity(intent);

            }
        });
    }


    //Liczba prawdobodobnie ilości tych okien do załadowania
    @Override
    public int getItemCount() {
        return this.bestObjectsList.size();
    }
}

