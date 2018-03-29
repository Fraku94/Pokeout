package com.example.pokeout.pokeout.Connect;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.Chat.ChatActivity;
import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UserDescryption.UserDescryptionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

/**
 * Created by Z710 on 2018-02-17.
 */

public class ConnectAdapter extends RecyclerView.Adapter<ConnectViewHolder>{
    private List<ConnectObject> connectObject;
    private Context context;

    public ConnectAdapter(List<ConnectObject> connectObject, Context context){
        this.connectObject = connectObject;
        this.context = context;
    }

    @Override
    public ConnectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Przypisanie wygladu okna do adaptera
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connect, null,false);

        //Ustawienie RecycleView
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        //Podpienie i zwrocenie wygladu
        ConnectViewHolder rcv = new ConnectViewHolder((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(final ConnectViewHolder holder, final int position) {

        holder.mConnectId = (connectObject.get(position).getId());

        //Ustawienie tekstu dla imienia
        holder.mName.setText(connectObject.get(position).getName());

        //Klikniecie w ikone dodawania kategori do obserwowanych
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.mDelete.showAnim();

                final int Position = holder.getAdapterPosition();

                final String CurrenUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String OtherUserId = connectObject.get(Position).getId();
                final ConnectObject deletedItem = connectObject.get(Position);

                GetChatId(CurrenUserId, OtherUserId, Position, holder, v, deletedItem);
            }
        });



        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView
        if(!connectObject.get(position).getImageUrl().equals("default")){
            Glide.with(context).load(connectObject.get(position).getImageUrl()).into(holder.mImage);
        }

        //Klikniecie w ikone idz do szczegolow uzytkownika
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Przekazanie danych potzrebnych do opisania szczegolow uzytkownika
                Intent intent = new Intent(v.getContext(), UserDescryptionActivity.class);
                Bundle b = new Bundle();
                b.putString("Id", connectObject.get(position).getId());
                b.putString("Name", connectObject.get(position).getName());
                b.putString("ImageUrl", connectObject.get(position).getImageUrl());
                b.putString("Descryption", connectObject.get(position).getDescryption());
                b.putString("Brith", connectObject.get(position).getBrith());
                b.putString("Sex", connectObject.get(position).getSex());
                b.putString("Phone", connectObject.get(position).getPhone());
                intent.putExtras(b);
                v.getContext().startActivity(intent);

            }
        });

        //Klikniecie w ikone idz do uzytkownikow z kategporii
        holder.mGointo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                Bundle b = new Bundle();
                b.putString("ConnectId", connectObject.get(position).getId());
                intent.putExtras(b);
                v.getContext().startActivity(intent);

            }
        });

    }
    @Override
    public int getItemCount() {
        return this.connectObject.size();
    }


    public void removeItem(int position) {
        connectObject.remove(position);

        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ConnectObject item, int position) {
        connectObject.add(position,item);
        // notify item added by position
        notifyItemInserted(position);
    }


    public void GetChatId(final String CurrenUserId, final String OtherUserId, final int Position, final RecyclerView.ViewHolder holder, final View v, final ConnectObject deletedItem){
        DatabaseReference GetChatId = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrenUserId).child("follow").child("connect").child(OtherUserId);

        GetChatId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String ChatId = "";

                if (dataSnapshot.child("ChatId").getValue() != null) {
                    ChatId = dataSnapshot.child("ChatId").getValue().toString();
                }

                GetToNext(CurrenUserId, OtherUserId, Position, ChatId, holder, v, deletedItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void GetToNext(final String CurrenUserId, final String OtherUserId, final int Position, final String ChatId, RecyclerView.ViewHolder holder, View v, final ConnectObject deletedItem) {
        final DatabaseReference Current = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrenUserId).child("follow");
        final DatabaseReference Other = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserId).child("follow");
        final DatabaseReference Chat = FirebaseDatabase.getInstance().getReference().child("Chat");

        // Usuniecie obecny uzytkownik
        Current.child("connect").child(OtherUserId).removeValue();
        Current.child("yes").child(OtherUserId).removeValue();
        Current.child("following").child("yes").child(OtherUserId).removeValue();

        // Usuniecie inny uzytkownik
        Other.child("connect").child(CurrenUserId).removeValue();
        Other.child("yes").child(CurrenUserId).removeValue();
        Other.child("following").child("yes").child(CurrenUserId).removeValue();

        // Usuniecie Czatu
        Chat.child(ChatId).removeValue();


        removeItem(Position);
        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar.make(v ," Usunoleś użytkownika!", Snackbar.LENGTH_LONG);
        snackbar.setAction("Cofnij", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
                restoreItem(deletedItem, Position);

                // Przywrocenie obecny uzytkownik
                Current.child("connect").child(OtherUserId).setValue(true);
                Current.child("yes").child(OtherUserId).setValue(true);
                Current.child("following").child("yes").child(OtherUserId).setValue(true);

                // Przywrocenie inny uzytkownik
                Other.child("connect").child(CurrenUserId).setValue(true);
                Other.child("yes").child(CurrenUserId).setValue(true);
                Other.child("following").child("yes").child(CurrenUserId).setValue(true);

                // Przywrocenie Czatu
                Chat.child(ChatId).setValue(true);

            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }
}
