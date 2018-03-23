package com.example.pokeout.pokeout.UsersInCategory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.Chat.ChatActivity;
import com.example.pokeout.pokeout.Connect.ConnectActivity;
import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UserDescryption.UserDescryptionActivity;
import com.example.pokeout.pokeout.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
    final private DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");


    final private String CurrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //Nadanie wartosci do okienek, pobranie ich z Objektow
    @Override
    public void onBindViewHolder(final UsersInCategoryViewHolder holder, final int position) {


        final String OtherUserID = usersInCategoryObjectsList.get(position).getId();
        Log.e("City", "City :    ffffffffff     "  );
        //Ustawienie tekstu dla imienia
        holder.mUserInCatName.setText(usersInCategoryObjectsList.get(position).getName());

        holder.mUserDistance.setText(usersInCategoryObjectsList.get(position).getDistance());
        Log.e("City", "position :         " + usersInCategoryObjectsList.get(position).getDistance()  );
//        holder.mUsersInCategoryMessage.setVisibility(View.INVISIBLE);
        holder.mCityUser.setText(usersInCategoryObjectsList.get(position).getCity());

        //Sprawdzenie i ustawienie czy uzytkwonika mamy juz dodanego czy nie (odpowiednia zmiana ikon)
        if(UserInformation.listFollowingUsers.contains(usersInCategoryObjectsList.get(position).getId())) {
            holder.mUserInCatYes.setVisibility(View.VISIBLE);
            holder.mUserInCatNo.setVisibility(View.INVISIBLE);
            holder.mUserInCatYes.setImageResource(R.drawable.like2);


        }else if (UserInformation.listNotFollowingUsers.contains(usersInCategoryObjectsList.get(position).getId())){

            holder.mUserInCatYes.setVisibility(View.INVISIBLE);
            holder.mUserInCatNo.setVisibility(View.VISIBLE);
            holder.mUserInCatNo.setImageResource(R.drawable.unlike);

        }else if (UserInformation.listIsConnectUsers.contains(usersInCategoryObjectsList.get(position).getId())) {

            holder.mUserInCatYes.setVisibility(View.INVISIBLE);
            holder.mUserInCatNo.setVisibility(View.INVISIBLE);
        }else{

            holder.mUserInCatYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mUserInCatYes.setVisibility(View.VISIBLE);
                    holder.mUserInCatNo.setVisibility(View.INVISIBLE);
                    holder.mUserInCatYes.setImageResource(R.drawable.like2);

                    usersDb.child(OtherUserID).child("follow").child("yes").child(CurrentUserID).setValue(true);
                    usersDb.child(CurrentUserID).child("follow").child("following").child("yes").child(OtherUserID).setValue(true);
                    isConnectionMatch(OtherUserID,holder);
                }
            });
            holder.mUserInCatNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mUserInCatYes.setVisibility(View.INVISIBLE);
                    holder.mUserInCatNo.setVisibility(View.VISIBLE);
                    holder.mUserInCatNo.setImageResource(R.drawable.unlike);
                    usersDb.child(OtherUserID).child("follow").child("no").child(CurrentUserID).setValue(true);
                    usersDb.child(CurrentUserID).child("follow").child("following").child("no").child(OtherUserID).removeValue();
                }
            });

        }


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
                b.putString("Id", usersInCategoryObjectsList.get(position).getId());
                b.putString("Name", usersInCategoryObjectsList.get(position).getName());
                b.putString("ImageUrl", usersInCategoryObjectsList.get(position).getImageUrl());
                b.putString("Descryption", usersInCategoryObjectsList.get(position).getDescryption());
                b.putString("Brith", usersInCategoryObjectsList.get(position).getBrith());
                b.putString("Sex", usersInCategoryObjectsList.get(position).getSex());
                b.putString("Phone", usersInCategoryObjectsList.get(position).getPhone());
                intent.putExtras(b);
                v.getContext().startActivity(intent);

            }
        });}

    //Klikniecie w ikone idz do czatu z uzytkownikiem
//        holder.mUsersInCategoryMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(v.getContext(), ConnectActivity.class);
//                v.getContext().startActivity(intent);
//
//            }
//        });
//    }
    //Sprawdzanie czy uzytkownicy sie połączyli jesli tak to wyswietla sie toast
    private void isConnectionMatch(String OtherUserID, final UsersInCategoryViewHolder holder) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(CurrentUserID).child("follow").child("yes").child(OtherUserID);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("follow").child("connect").child(CurrentUserID).child("ChatId").setValue(key);
                    usersDb.child(CurrentUserID).child("follow").child("connect").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                    Toast.makeText(context, "Is Conenct", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "commect get");

//                    holder.mUsersInCategoryMessage.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Liczba prawdobodobnie ilości tych okien do załadowania
    @Override
    public int getItemCount() {
        return this.usersInCategoryObjectsList.size();
    }
}