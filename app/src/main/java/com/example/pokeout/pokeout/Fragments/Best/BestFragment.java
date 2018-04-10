package com.example.pokeout.pokeout.Fragments.Best;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokeout.pokeout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BestFragment extends Fragment {

    //Odswiezanie
    private SwipeRefreshLayout swipeRefreshLayout;

    public BestFragment() {

    }

    Context mContext;
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
//        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.shape_border));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
//                itemDecorator.VERTICAL));

        DividerItemDecoration divider = new
                DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.shape_border));
        mRecyclerView.addItemDecoration(divider);
        //Przypisanie funkicji odswiezania
        swipeRefreshLayout = rootView.findViewById(R.id.swipeContainerBest);

        //Inicjacja odswierzania
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                clear();
                mBestAdapter = new BestAdapter(getDataSetBest(),getContext());
                mRecyclerView.setAdapter(mBestAdapter);

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
        int size = this.resoultBest.size();
        this.resoultBest.clear();
        mBestAdapter.notifyItemRangeChanged(0, size);
    }

    //Przeslanie do Adaptera Rezultatow
    private ArrayList<BestObject> resoultBest = new ArrayList<BestObject>();

    private List<BestObject> getDataSetBest() {

        //Tu Startuje fragment
        getCategoryId();

        return resoultBest;

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
                    FetchBestInformation(dataSnapshot.getChildrenCount(),key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void FetchBestInformation(final Long count, final String key) {

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
                    BestObject object = new BestObject(categoryId, name, categoryImageUrl, descryption, Count);

                    //Metoda dodawania do Objektu
                    resoultBest.add(object);

                    //Metoda notujaca zmiany (Wywoluje zapisanie zmiennych)
                    mBestAdapter.notifyDataSetChanged();

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




//getChildrenCount ()