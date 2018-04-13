package com.example.pokeout.pokeout.CategoryDescryption;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.MainActivity;
import com.example.pokeout.pokeout.R;

import java.util.List;

/**
 * Created by Z710 on 2018-02-27.
 */

public class CategoryDescryptionAdapter extends RecyclerView.Adapter<CategoryDescryptionViewHolder>{

    private List<CategoryDescryptionObject> categoryDescryptionObjectsList;

    private Context context;

    //Przypisanie Obiektów do adaptera
    public CategoryDescryptionAdapter(List<CategoryDescryptionObject> categoryDescryptionObjectsList, Context context){
        this.categoryDescryptionObjectsList = categoryDescryptionObjectsList;
        this.context = context;
    }

    //Tworzenie wyglądu i przypisanie ViewHoldera do adaptera
    @Override
    public CategoryDescryptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        //Przypisanie wygladu okna do adaptera
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_descryption, null,false);

        //Ustawienie RecycleView
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        //Podpienie i zwrocenie wygladu
        CategoryDescryptionViewHolder rcv = new CategoryDescryptionViewHolder((layoutView));

        return rcv;
    }
    //Nadanie wartosci do okienek, pobranie ich z Objektow
    @Override
    public void onBindViewHolder(final CategoryDescryptionViewHolder holder, final int position) {

        //Ustawienie tekstu dla imienia
        holder.mCategoryDescryptionName.setText(categoryDescryptionObjectsList.get(position).getName());

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView
        if(!categoryDescryptionObjectsList.get(position).getImageUrl().equals("default")){
            Glide.with(context).load(categoryDescryptionObjectsList.get(position).getImageUrl()).into(holder.mCategoryDescryptionImage);
        }

        if (categoryDescryptionObjectsList.size() == 0){

            Toast.makeText(context,"Nie ma wiecej propozycji",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        }
    }

    //Liczba prawdobodobnie ilości tych okien do załadowania
    @Override
    public int getItemCount() {
        return this.categoryDescryptionObjectsList.size();
    }
}