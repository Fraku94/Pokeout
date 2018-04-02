package com.example.pokeout.pokeout.UsersInCategory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UserDescryption.UserDescryptionActivity;
import com.example.pokeout.pokeout.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Z710 on 2018-02-26.
 */

public class UsersInCategoryAdapter extends RecyclerView.Adapter<UsersInCategoryViewHolder> {

    private List<UsersInCategoryObject> usersInCategoryObjectsList;

    private Context context;
    private static final int EMPTY_VIEW = 10;

    //Przypisanie Obiektów do adaptera
    public UsersInCategoryAdapter(List<UsersInCategoryObject> usersInCategoryObjectsList, Context context) {
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

        final AlphaAnimation buttonClick = new AlphaAnimation(0.2f, 1.0f);
        buttonClick.setDuration(1000);
        buttonClick.setStartOffset(5000);
        buttonClick.setFillAfter(true);


      //  Log.e("City", "City :    ffffffffff     "  );
        //Ustawienie tekstu dla imienia
        holder.mUserInCatName.setText(usersInCategoryObjectsList.get(position).getName());

        holder.mUserDistance.setText(usersInCategoryObjectsList.get(position).getName() + "km");
     //   Log.e("City", "position :         " + usersInCategoryObjectsList.get(position).getDistance()  );
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
                    holder.mUserInCatYes.startAnimation(buttonClick);
//                    v.startAnimation(buttonClick);
                    holder.mUserInCatYes.setVisibility(View.VISIBLE);
                    holder.mUserInCatNo.setVisibility(View.INVISIBLE);
                    holder.mUserInCatYes.setImageResource(R.drawable.like2);

                    int Position = holder.getAdapterPosition();
                    String OtherUserID = usersInCategoryObjectsList.get(Position).getId();

                    usersDb.child(OtherUserID).child("follow").child("yes").child(CurrentUserID).setValue(true);
                    usersDb.child(CurrentUserID).child("follow").child("following").child("yes").child(OtherUserID).setValue(true);


                    isConnectionMatch(OtherUserID);

                    removeItem(Position);


                    if (usersInCategoryObjectsList.size() == 0){
                        Toast.makeText(context,"Nie ma wiecej propozycji",Toast.LENGTH_LONG).show();

                    }
                }
            });
            holder.mUserInCatNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mUserInCatYes.setVisibility(View.INVISIBLE);
                    holder.mUserInCatNo.setVisibility(View.VISIBLE);
                    holder.mUserInCatNo.setImageResource(R.drawable.unlike);

                    int Position = holder.getAdapterPosition();
                    String OtherUserID = usersInCategoryObjectsList.get(Position).getId();

                    usersDb.child(OtherUserID).child("follow").child("no").child(CurrentUserID).setValue(true);
                    usersDb.child(CurrentUserID).child("follow").child("following").child("no").child(OtherUserID).removeValue();

                    removeItem(Position);


                    if (usersInCategoryObjectsList.size() == 0){
                        Toast.makeText(context,"Nie ma wiecej propozycji",Toast.LENGTH_LONG).show();

                    }
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
        });

    }

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
    private void isConnectionMatch(final String OtherUserID) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(CurrentUserID).child("follow").child("yes").child(OtherUserID);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String chatKey = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("follow").child("connect").child(CurrentUserID).child("ChatId").setValue(chatKey);
                    usersDb.child(CurrentUserID).child("follow").child("connect").child(dataSnapshot.getKey()).child("ChatId").setValue(chatKey);
                    Toast.makeText(context, "Is Conenct", Toast.LENGTH_SHORT).show();


                    String CurrentUserToken = FirebaseInstanceId.getInstance().getToken();


                    DatabaseReference NotificationDb = FirebaseDatabase.getInstance().getReference().child("Notification").child(OtherUserID);
                    DatabaseReference newNotificationDb = NotificationDb.push();
                    Map newNotification = new HashMap();
                    newNotification.put("createdByUser", CurrentUserID);
                    newNotification.put("token", CurrentUserToken);
                    newNotification.put("type", "Connect");

                    newNotificationDb.setValue(newNotification);

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

    public void removeItem(int position) {
        usersInCategoryObjectsList.remove(position);

        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
}