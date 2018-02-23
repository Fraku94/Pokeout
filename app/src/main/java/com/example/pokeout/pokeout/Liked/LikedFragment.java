package com.example.pokeout.pokeout.Liked;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokeout.pokeout.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LikedFragment extends Fragment {
    public LikedFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mLikedAdapter;
    private RecyclerView.LayoutManager mLikedLayoutMenager;
    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup                  container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_liked, container, false);

        //Pobranie Conextu dla fragmentu
        Context context = getActivity();

        //Pobranie ID obecnego uzytkownika
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Ustawienie RecycleView
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        //Ustawienie Adaptera oraz LayoutMenagera. Uzycie Contextu fragmentu
        mLikedLayoutMenager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLikedLayoutMenager);
        mLikedAdapter = new LikedAdapter(getDataSetLiked(),context);
        mRecyclerView.setAdapter(mLikedAdapter);
        clear();
        //Wywolanie metody by uzyskac ID Kategorii
        getCategoryId();


        //zwrocenie wygladu
        return rootView;
    }

    private void getCategoryId() {

        //Referencja do bazy Users>>userID>>category
        DatabaseReference likeddb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("category");
        likeddb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Sprawdzenie czy istnieje
                if (dataSnapshot.exists()) {

                    //pobranie wartości z "category"
                    for (DataSnapshot category : dataSnapshot.getChildren()) {

                        //Wywolanie metody zbierajacej informacje o kategorii z przekazaniem w niej ID danej kategorii
                        //getKey() pobiera ID kategorii
                        FetchLikedInformation(category.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void FetchLikedInformation(String key) {

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

                    //Pobranie warosci name jesli nie jest pusta i przypisanie do zmiennej
                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }

                    //Pobranie warosci linku zdjecia jesli nie jest pusty i przypisanie do zmiennej
                    if (dataSnapshot.child("categoryImageUrl").getValue() != null) {
                        categoryImageUrl = dataSnapshot.child("categoryImageUrl").getValue().toString();
                    }

                    //Dodanie zmeinnych do Obiektu (nazwy musza byc takie same jak w Objekcie
                    LikedObject object = new LikedObject(categoryId, name, categoryImageUrl);

                    //Metoda dodawania do Objektu
                    resoultLiked.add(object);

                    //Metoda notujaca zmiany (Wywoluje zapisanie zmiennych)
                    mLikedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void clear() {
        int size = this.resoultLiked.size();
        this.resoultLiked.clear();
        mLikedAdapter.notifyItemRangeChanged(0, size);
    }
    //Przeslanie do Adaptera Rezultatow
    private ArrayList<LikedObject> resoultLiked = new ArrayList<LikedObject>();

    private List<LikedObject> getDataSetLiked() {

        return resoultLiked;

    }

}