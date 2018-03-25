package com.example.pokeout.pokeout.Fragments.Liked;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
 * Created by Z710 on 2018-02-22.
 */


public class LikedAdapter extends RecyclerView.Adapter<LikedViewHolder>{

    private List<LikedObject> likedList;
    private List<LikedObject> likedListFiltered;
    private Context context;


    //Przypisanie Obiektów do adaptera
    public LikedAdapter(List<LikedObject> likedList, Context context){
        this.likedList = likedList;
        this.context = context;
        this.likedListFiltered = likedList;
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
    public void onBindViewHolder(final LikedViewHolder holder, final int position) {

        holder.mCategoryId = likedList.get(position).getCategoryId();
        holder.mCategoryName.setText(likedList.get(position).getName());
        holder.mCategoryCount.setText(likedList.get(position).getCount());


//Sprawdzenie i ustawienie czy kategorie mamy juz dodana czy nie (odpowiednia zmiana ikon)
        if(CategoryInformation.listFollowingCategory.contains(likedList.get(position).getCategoryId())){
            holder.mGointo.setVisibility(View.VISIBLE);
        }

        //Klikniecie w ikone dodawania kategori do obserwowanych
        holder.mLikedFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.mLikedFollow.showAnim();
                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final int Position = holder.getAdapterPosition();
                final LikedObject deletedItem = likedList.get(Position);
                final String DelId = likedList.get(Position).getCategoryId();


                //Jesli w "categoryInformation" jest id kategorii to ma ja usunac i zmienic odpowiednio ikony
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("category").child(likedList.get(Position).getCategoryId()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Category").child(likedList.get(Position).getCategoryId()).child("users").child(userId).removeValue();
                removeItem(Position);
                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(v ,holder.mCategoryName.getText() + " Usunoleś kategorie!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // undo is selected, restore the deleted item
                        restoreItem(deletedItem, Position);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("category").child(DelId).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Category").child(DelId).child("users").child(userId).setValue(true);

                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        });

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView
        if(!likedList.get(position).getCategoryImageUrl().equals("default")){
            Glide.with(context).load(likedList.get(position).getCategoryImageUrl()).into(holder.mCategoryImage);
        }

        //Klikniecie w ikone idz do szczegolow kategorii
        holder.mCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Przekazanie danych potzrebnych do opisania szczegolow kategorii
                Intent intent = new Intent(v.getContext(), CategoryDescryptionActivity.class);
                Bundle b = new Bundle();
                b.putString("Id", likedList.get(position).getCategoryId());
                b.putString("Name", likedList.get(position).getName());
                b.putString("ImageUrl", likedList.get(position).getCategoryImageUrl());
                b.putString("Descryption", likedList.get(position).getCatDescryption());
                intent.putExtras(b);
                v.getContext().startActivity(intent);

            }
        });

        //Klikniecie w ikone idz do uzytkownikow z kategporii
        holder.mGointo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), UsersInCategoryActivity.class);
                Bundle b = new Bundle();
                b.putString("CategoryId", likedList.get(position).getCategoryId());
                intent.putExtras(b);
                v.getContext().startActivity(intent);

            }
        });
    }


    //Liczba prawdobodobnie ilości tych okien do załadowania
    @Override
    public int getItemCount() {
        return this.likedList.size();
    }

    public void removeItem(int position) {
        likedList.remove(position);

        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(LikedObject item, int position) {
        likedList.add(position,item);
        // notify item added by position
        notifyItemInserted(position);
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                String charString = charSequence.toString();
//                if (charString.isEmpty()) {
//                    likedListFiltered = likedList;
//                } else {
//                    List<LikedObject> filteredList = new ArrayList<>();
//                    for (LikedObject row : likedList) {
//
//                        // name match condition. this might differ depending on your requirement
//                        // here we are looking for name or phone number match
//                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
//                            filteredList.add(row);
//                        }
//                    }
//
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = likedListFiltered;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                likedListFiltered = (ArrayList<LikedObject>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }
}

