package com.example.pokeout.pokeout.UsersInCategory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.Chat.ChatActivity;
import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UserDescryption.UserDescryptionActivity;
import com.example.pokeout.pokeout.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Z710 on 2018-02-26.
 */

public class UsersInCategoryAdapter extends RecyclerView.Adapter<UsersInCategoryViewHolder>{

        private List<UsersInCategoryObject> usersInCategoryObjectsList;

        private Context context;

        //Przypisanie Obiektów do adaptera
        public UsersInCategoryAdapter(List<UsersInCategoryObject> usersInCategoryObjectsList, Context context){
            this.usersInCategoryObjectsList = usersInCategoryObjectsList;
            this.context = context;
        }

        //Tworzenie wyglądu i przypisanie ViewHoldera do adaptera
        @Override
        public UsersInCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



            //Przypisanie wygladu okna do adaptera
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_in_category, null,false);

            //Ustawienie RecycleView
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);

            //Podpienie i zwrocenie wygladu
            UsersInCategoryViewHolder rcv = new UsersInCategoryViewHolder((layoutView));

            return rcv;
        }
    //Nadanie wartosci do okienek, pobranie ich z Objektow
    @Override
    public void onBindViewHolder(final UsersInCategoryViewHolder holder, final int position) {

        //Ustawienie tekstu dla imienia
        holder.mUserInCatName.setText(usersInCategoryObjectsList.get(position).getName());

        //Sprawdzenie i ustawienie czy uzytkwonika mamy juz dodanego czy nie (odpowiednia zmiana ikon)
        if(UserInformation.listFollowingUsers.contains(usersInCategoryObjectsList.get(position).getId())){
            holder.mUserInCatBFollow.setImageResource(R.mipmap.ic_remove_user);
            holder.mUsersInCategoryMessage.setVisibility(View.VISIBLE);
        }else {
            holder.mUserInCatBFollow.setImageResource(R.mipmap.ic_add_user);
            holder.mUsersInCategoryMessage.setVisibility(View.INVISIBLE);
        }

        //Klikniecie w ikone dodawania uzytkwonika do obserwowanych
        holder.mUserInCatBFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Przypisanie ID obecnego uzytkwonika
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                //Jesli w "UserInformation" nie ma id uzytkownika to ma ja doda i zmienic odpowiednio ikony
                if(!UserInformation.listFollowingUsers.contains(usersInCategoryObjectsList.get(position).getId())){
                    holder.mUserInCatBFollow.setImageResource(R.mipmap.ic_remove_user);
                    holder.mUsersInCategoryMessage.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("following").child(usersInCategoryObjectsList.get(position).getId()).setValue(true);
                }

                //Jesli w "UserInformation" jest id uzytkownika to ma ja usunac i zmienic odpowiednio ikony
                else{
                    holder.mUserInCatBFollow.setImageResource(R.mipmap.ic_add_user);
                    holder.mUsersInCategoryMessage.setVisibility(View.INVISIBLE);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("following").child(usersInCategoryObjectsList.get(position).getId()).removeValue();
                }
            }
        });
        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView
        if(!usersInCategoryObjectsList.get(position).getImageUrl().equals("default")){
            Glide.with(context).load(usersInCategoryObjectsList.get(position).getImageUrl()).into(holder.mUserInCatImage);
        }

        //Klikniecie w ikone idz do szczegolow uzytkownika
        holder.mUserInCatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Przekazanie danych potzrebnych do opisania szczegolow uzytkownika
                Intent intent = new Intent(v.getContext(), UserDescryptionActivity.class);
                Bundle b = new Bundle();
                b.putString("Name", usersInCategoryObjectsList.get(position).getName());
                b.putString("ImageUrl", usersInCategoryObjectsList.get(position).getImageUrl());
                b.putString("Descryption", usersInCategoryObjectsList.get(position).getDescryption());
                b.putString("Brith", usersInCategoryObjectsList.get(position).getBrith());
                b.putString("Sex", usersInCategoryObjectsList.get(position).getSex());
                b.putString("Phone", usersInCategoryObjectsList.get(position).getPhone());
                intent.putExtras(b);
                v.getContext().startActivity(intent);

            }
        });

        //Klikniecie w ikone idz do czatu z uzytkownikiem
        holder.mUsersInCategoryMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                v.getContext().startActivity(intent);

            }
        });
    }

        //Liczba prawdobodobnie ilości tych okien do załadowania
        @Override
        public int getItemCount() {
            return this.usersInCategoryObjectsList.size();
        }
}
