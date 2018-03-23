package com.example.pokeout.pokeout.Fragments.Liked;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokeout.pokeout.CategoryAdd.CategoryAddActivity;
import com.example.pokeout.pokeout.R;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LikedFragment extends Fragment {

    //Odswiezanie
    private SwipeRefreshLayout swipeRefreshLayout;

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

        //Przypisanie buttona dodawania kategorii


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



        //Przypisanie funkicji odswiezania
        swipeRefreshLayout = rootView.findViewById(R.id.swipeContainerLiked);

        //Inicjacja odswierzania
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                clear();
                mLikedAdapter = new LikedAdapter(getDataSetLiked(),getContext());
                mRecyclerView.setAdapter(mLikedAdapter);

            }
        });

        //Style kolka odswiezania
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Czyszczenie fragmentu gdy jest automatycznie przesowany
        clear();

        //zwrocenie wygladu
        return rootView;
    }

    //Czyszczenie fragmentu
    private void clear() {
        int size = this.resoultLiked.size();
        this.resoultLiked.clear();
        mLikedAdapter.notifyItemRangeChanged(0, size);
    }

    //Przeslanie do Adaptera Rezultatow
    private ArrayList<LikedObject> resoultLiked = new ArrayList<LikedObject>();

    private List<LikedObject> getDataSetLiked() {
        //Tu Startuje fragment
        getCategoryId();
        return resoultLiked;
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
                        getCategoryCount(category.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCategoryCount(final String key) {

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
                    String Count = Long.toString(count);


                    //Pobranie warosc name jesli nie jest pusta i przypisanie do zmiennej
                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }

                    //Pobranie warosci linku zdjecia jesli nie jest pusty i przypisanie do zmiennej
                    if (dataSnapshot.child("categoryImageUrl").getValue() != null) {
                        categoryImageUrl = dataSnapshot.child("categoryImageUrl").getValue().toString();
                    }


                    //Dodanie zmeinnych do Obiektu (nazwy musza byc takie same jak w Objekcie
                    LikedObject object = new LikedObject(categoryId, name, categoryImageUrl,Count);

                    //Metoda dodawania do Objektu
                    resoultLiked.add(object);

                    //Metoda notujaca zmiany (Wywoluje zapisanie zmiennych)
                    mLikedAdapter.notifyDataSetChanged();

                    //Zatrzymanie animacji wyszukiwania
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}