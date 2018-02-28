package com.example.pokeout.pokeout.Suggest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokeout.pokeout.Best.BestAdapter;
import com.example.pokeout.pokeout.Best.BestObject;
import com.example.pokeout.pokeout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SuggestFragment extends Fragment {


    public SuggestFragment() {

    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mSuggestAdapter;
    private RecyclerView.LayoutManager mSuggestLayoutMenager;
    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup                  container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_suggest, container, false);

        //Pobranie Conextu dla fragmentu
        Context context = getActivity();

        //Pobranie ID obecnego uzytkownika
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Ustawienie RecycleView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.suggestRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        //Ustawienie Adaptera oraz LayoutMenagera. Uzycie Contextu fragmentu
        mSuggestLayoutMenager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mSuggestLayoutMenager);
        mSuggestAdapter = new SuggestAdapter(getDataSetSuggest(),context);
        mRecyclerView.setAdapter(mSuggestAdapter);

        //Czyszczenie recycleview
        clear();

        //Wywolanie metody by uzyskac ID Kategorii
        getCategoryId();

        //zwrocenie wygladu
        return rootView;
    }
    private void getCategoryId() {

        //Referencja do bazy z sugerowanymi kategoriami
        final String adminId = "UYjTdbeg7zXSwXFhN72eDPseU1R2";
        DatabaseReference suggestdb = FirebaseDatabase.getInstance().getReference().child("Users").child(adminId).child("category");
        suggestdb.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    if (dataSnapshot.child("description").getValue() != null) {
                        descryption = dataSnapshot.child("description").getValue().toString();
                    }

                    //Dodanie zmeinnych do Obiektu (nazwy musza byc takie same jak w Objekcie
                    SuggestObject object = new SuggestObject(categoryId, name, categoryImageUrl, descryption, Count);

                    //Metoda dodawania do Objektu
                    resoultSuggest.add(object);

                    //Metoda notujaca zmiany (Wywoluje zapisanie zmiennych)
                    mSuggestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void clear() {
        int size = this.resoultSuggest.size();
        this.resoultSuggest.clear();
        mSuggestAdapter.notifyItemRangeChanged(0, size);
    }
    //Przeslanie do Adaptera Rezultatow
    private ArrayList<SuggestObject> resoultSuggest = new ArrayList<SuggestObject>();

    private List<SuggestObject> getDataSetSuggest() {

        return resoultSuggest;

    }


}