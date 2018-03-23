package com.example.pokeout.pokeout.Fragments.Suggest;

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
import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UsersInCategory.UsersInCategoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Z710 on 2018-02-27.
 */

public class SuggestAdapter extends RecyclerView.Adapter<SuggestViewHolder>{

private List<SuggestObject> suggestObjectsList;

private Context context;

//Przypisanie Obiektów do adaptera
public SuggestAdapter(List<SuggestObject> suggestObjectsList, Context context){
        this.suggestObjectsList = suggestObjectsList;
        this.context = context;
        }

//Tworzenie wyglądu i przypisanie ViewHoldera do adaptera
@Override
public SuggestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        //Przypisanie wygladu okna do adaptera
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest, null,false);

        //Ustawienie RecycleView
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        //Podpienie i zwrocenie wygladu
        SuggestViewHolder rcv = new SuggestViewHolder(layoutView);

        return rcv;
        }
//Nadanie wartosci do okienek, pobranie ich z Objektow
@Override
public void onBindViewHolder(final SuggestViewHolder holder, final int position) {

        //Ustawienie tekstu dla imienia
        holder.mSuggestName.setText(suggestObjectsList.get(position).getName());

        //Ustawienie tekstu dla wartosci liczby obserwujacych uzytkownikow
        holder.mSuggestCount.setText(suggestObjectsList.get(position).getCount());

        //Sprawdzenie i ustawienie czy kategorie mamy juz dodana czy nie (odpowiednia zmiana ikon)
        if(CategoryInformation.listFollowingCategory.contains(suggestObjectsList.get(position).getId())){
        holder.mSuggestFollow.setImageResource(R.drawable.unffalow);
//        holder.mSuggestGo.setVisibility(View.VISIBLE);
        }else {
        holder.mSuggestFollow.setImageResource(R.drawable.like2);
//        holder.mSuggestGo.setVisibility(View.INVISIBLE);
        }

        //Klikniecie w ikone dodawania kategori do obserwowanych
        holder.mSuggestFollow.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        //Przypisanie ID obecnego uzytkwonika
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Jesli w "categoryInformation" nie ma id kategorii to ma ja doda i zmienic odpowiednio ikony
        if(!CategoryInformation.listFollowingCategory.contains(suggestObjectsList.get(position).getId())){
        holder.mSuggestFollow.setImageResource(R.drawable.unffalow);
//        holder.mSuggestGo.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("category").child(suggestObjectsList.get(position).getId()).setValue(true);
        FirebaseDatabase.getInstance().getReference().child("Category").child(suggestObjectsList.get(position).getId()).child("users").child(userId).setValue(true);
        }

        //Jesli w "categoryInformation" jest id kategorii to ma ja usunac i zmienic odpowiednio ikony
        else{
        holder.mSuggestFollow.setImageResource(R.drawable.like2);
//        holder.mSuggestGo.setVisibility(View.INVISIBLE);
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("category").child(suggestObjectsList.get(position).getId()).removeValue();
        FirebaseDatabase.getInstance().getReference().child("Category").child(suggestObjectsList.get(position).getId()).child("users").child(userId).removeValue();
        }
        }
        });

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView
        if(!suggestObjectsList.get(position).getImageUrl().equals("default")){
        Glide.with(context).load(suggestObjectsList.get(position).getImageUrl()).into(holder.mSuggestImage);
        }

        //Klikniecie w ikone idz do szczegolow kategorii
        holder.mSuggestImage.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        //Przekazanie danych potzrebnych do opisania szczegolow kategorii
        Intent intent = new Intent(v.getContext(), CategoryDescryptionActivity.class);
        Bundle b = new Bundle();
        b.putString("Id", suggestObjectsList.get(position).getId());
        b.putString("Name", suggestObjectsList.get(position).getName());
        b.putString("ImageUrl", suggestObjectsList.get(position).getImageUrl());
        b.putString("Descryption", suggestObjectsList.get(position).getCatDescryption());
        intent.putExtras(b);
        v.getContext().startActivity(intent);

        }
        });

        //Klikniecie w ikone idz do uzytkownikow z kategporii
//        holder.mSuggestGo.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//
//        Intent intent = new Intent(v.getContext(), UsersInCategoryActivity.class);
//        Bundle b = new Bundle();
//        b.putString("CategoryId", suggestObjectsList.get(position).getId());
//        intent.putExtras(b);
//        v.getContext().startActivity(intent);
//
//        }
//        });
        }


//Liczba prawdobodobnie ilości tych okien do załadowania
@Override
public int getItemCount() {
        return this.suggestObjectsList.size();
        }
        }