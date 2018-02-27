package com.example.pokeout.pokeout.Best;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokeout.pokeout.CategoryAdd.CategoryAddActivity;
import com.example.pokeout.pokeout.Liked.LikedAdapter;
import com.example.pokeout.pokeout.Liked.LikedObject;
import com.example.pokeout.pokeout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BestFragment extends Fragment {
    public BestFragment() {

    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mBestAdapter;
    private RecyclerView.LayoutManager mBestLayoutMenager;
    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup                  container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_best, container, false);

        //Pobranie Conextu dla fragmentu
        Context context = getActivity();

        //Pobranie ID obecnego uzytkownika
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Ustawienie RecycleView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.bestRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        //Ustawienie Adaptera oraz LayoutMenagera. Uzycie Contextu fragmentu
        mBestLayoutMenager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mBestLayoutMenager);
        mBestAdapter = new BestAdapter(getDataSetBest(),context);
        mRecyclerView.setAdapter(mBestAdapter);

        //Czyszczenie recycleview
        clear();

        //Wywolanie metody by uzyskac ID Kategorii
        getCategoryId();

        //zwrocenie wygladu
        return rootView;
    }
    private void getCategoryId() {

        //Referencja do bazy Category
        DatabaseReference bestdb = FirebaseDatabase.getInstance().getReference().child("Category");
        bestdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Sprawdzenie czy istnieje
                if (dataSnapshot.exists()) {

                    //pobranie wartości z "category"
                    for (DataSnapshot category : dataSnapshot.getChildren()) {

                        //Wywolanie metody zbierajacej informacje o kategorii z przekazaniem w niej ID danej kategorii
                        //getKey() pobiera ID kategorii
                        getCategoryCount(category.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Pobranie liczby uzytkownikow danej kategorii
    private void getCategoryCount(final String key) {

        //Referencja do Category>>categoryID>>users
        DatabaseReference countdb = FirebaseDatabase.getInstance().getReference().child("Category").child(key).child("users");
        countdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FetchLikedInformation(dataSnapshot.getChildrenCount(),key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void FetchLikedInformation(final Long count, final String key) {

        //Referencja do bazy Category>>categoryID. przekazanie zmiennyej categoryID z getCategoryId(); w metodzie jako key
        DatabaseReference categoryDb = FirebaseDatabase.getInstance().getReference().child("Category").child(key);
        categoryDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Sprawdzenie czy istnieje
                if (dataSnapshot.exists()) {

                    //Przypisanie wartosci zmiennym
                    //getKey() pobiera ID kategorii
                    String categoryId = dataSnapshot.getKey();
                    String name = "";
                    String categoryImageUrl = "";
                    String descryption = "";
                    String Count = Long.toString(count);

                    //Pobranie warosci name jesli nie jest pusta i przypisanie do zmiennej
                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }

                    //Pobranie warosci linku zdjecia jesli nie jest pusty i przypisanie do zmiennej
                    if (dataSnapshot.child("categoryImageUrl").getValue() != null) {
                        categoryImageUrl = dataSnapshot.child("categoryImageUrl").getValue().toString();
                    }

                    //Pobranie warosci linku zdjecia jesli nie jest pusty i przypisanie do zmiennej
                    if (dataSnapshot.child("descryption").getValue() != null) {
                        descryption = dataSnapshot.child("descryption").getValue().toString();
                    }

                    //Dodanie zmeinnych do Obiektu (nazwy musza byc takie same jak w Objekcie
                    BestObject object = new BestObject(categoryId, name, categoryImageUrl, descryption, Count);

                    //Metoda dodawania do Objektu
                    resoultBest.add(object);

                    //Metoda notujaca zmiany (Wywoluje zapisanie zmiennych)
                    mBestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void clear() {
        int size = this.resoultBest.size();
        this.resoultBest.clear();
        mBestAdapter.notifyItemRangeChanged(0, size);
    }
    //Przeslanie do Adaptera Rezultatow
    private ArrayList<BestObject> resoultBest = new ArrayList<BestObject>();

    private List<BestObject> getDataSetBest() {

        return resoultBest;

    }


}




//getChildrenCount ()