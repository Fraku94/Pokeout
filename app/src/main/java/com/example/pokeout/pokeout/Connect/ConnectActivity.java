package com.example.pokeout.pokeout.Connect;


import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConnectActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mConnectAdapter;
    private RecyclerView.LayoutManager mConnectLayoutMenager;

    private String CurrentUserId, formattedDistanceString;

    private int i=0;
    TextView tvNoMore;

    private DatabaseReference LocationRef;
    double locationLat1,locationLat2;
    double locationLng1, locationLng2;
    public Location Loc1, Loc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mRecyclerview = findViewById(R.id.recyclerViewConnect);
        mRecyclerview.setScrollbarFadingEnabled(false);
        mRecyclerview.setHasFixedSize(true);
        tvNoMore= findViewById(R.id.tvNoMoreConnect);
        //ID obecnego Uzytkownika
        CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Przypisanie Layoutu
        mConnectLayoutMenager = new LinearLayoutManager(ConnectActivity.this);
        mRecyclerview.setLayoutManager(mConnectLayoutMenager);
        mConnectAdapter = new ConnectAdapter(getDataSetConnect(), ConnectActivity.this);
        mRecyclerview.setAdapter(mConnectAdapter);
        LocationRef = FirebaseDatabase.getInstance().getReference().child("location");
        getUserConnectId();

    }

    //pobieranie id uzytkownika z ktorym dalismy sobei TAK
    private void getUserConnectId() {

        DatabaseReference Connectdb = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId).child("follow").child("connect");


        Connectdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    i=1;
                    for (DataSnapshot connect : dataSnapshot.getChildren()) {
                        getDistance(connect.getKey());
                    }
                }

                if (i == 0){
                    i=1;

                    tvNoMore.setVisibility(View.VISIBLE);
                    Log.e("traf0", "i = 0 ");
                }else if(i == 1){
                    i=2;
                    Log.e("traf0", "i = 1 ");
                    tvNoMore.setVisibility(View.INVISIBLE);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDistance(final String OtherUserId) {

        LocationRef.child(CurrentUserId).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    List<Object> map = (List<Object>) dataSnapshot.getValue();

                    //Pobranie promienia
                    if (map.get(0) != null) {
                        locationLat1 = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng1 = Double.parseDouble(map.get(1).toString());
                    }

                    //Ustawienie lokalizacji uzytkownika
                    Loc1 = new Location("");
                    Loc1.setLatitude(locationLat1);
                    Loc1.setLongitude(locationLng1);

                    //Metoda pobiera uzytkownikow znajdujacych sie w promienu
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LocationRef.child(OtherUserId).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    List<Object> map = (List<Object>) dataSnapshot.getValue();

                    //Pobranie promienia
                    if (map.get(0) != null) {
                        locationLat2 = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng2 = Double.parseDouble(map.get(1).toString());
                    }

                    //Ustawienie lokalizacji uzytkownika
                    Loc2 = new Location("");
                    Loc2.setLatitude(locationLat2);
                    Loc2.setLongitude(locationLng2);

                    float distance = Loc1.distanceTo(Loc2)/1000;
                    formattedDistanceString = String.format("%.1f", distance);


                    FetchConnectInformation(OtherUserId,formattedDistanceString);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Dodawanie informacji o uzytkowniku co dal nam rowniez tak
    private void FetchConnectInformation(String OtherUserId, final String Distance) {

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserId);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String Id = dataSnapshot.getKey();
                    String Name = "";
                    String ImageUrl = "";
                    String Description = "";
                    String Brith = "";
                    String Sex = "";
                    String Phone = "";
                    String City = "";
                    if (dataSnapshot.child("name").getValue() != null) {
                        Name = dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("profileImageUrl").getValue() != null) {
                        ImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    if (dataSnapshot.child("description").getValue() != null) {
                        Description = dataSnapshot.child("description").getValue().toString();
                    }
                    if (dataSnapshot.child("brith").getValue() != null) {
                        Brith = dataSnapshot.child("brith").getValue().toString();
                    }
                    if (dataSnapshot.child("sex").getValue() != null) {
                        Sex = dataSnapshot.child("sex").getValue().toString();
                    }
                    if (dataSnapshot.child("phone").getValue() != null) {
                        Phone = dataSnapshot.child("phone").getValue().toString();
                    }
                    if (dataSnapshot.child("city").getValue() != null) {
                        City = dataSnapshot.child("city").getValue().toString();
                    }

                    //przypisanie do obiektu zmiennych
                    ConnectObject obj = new ConnectObject(Id, Name, ImageUrl, Description, Brith, Sex, Phone,Distance,City);
                    resultsConnect.add(obj);
                    mConnectAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    //rezultaty matches
    private ArrayList<ConnectObject> resultsConnect = new ArrayList<ConnectObject>();
    private List<ConnectObject> getDataSetConnect() {
        return resultsConnect;
    }

}