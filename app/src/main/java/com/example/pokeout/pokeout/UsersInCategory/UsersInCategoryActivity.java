package com.example.pokeout.pokeout.UsersInCategory;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pokeout.pokeout.Connect.ConnectActivity;
import com.example.pokeout.pokeout.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;

import com.firebase.geofire.GeoQuery;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UsersInCategoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mUsersInCategoryAdapter;
    private RecyclerView.LayoutManager mUsersInCategoryLayoutMenager;

    private ArrayList<UsersInCategoryObject> resoultUsersInCategory = new ArrayList<UsersInCategoryObject>();

    private FirebaseAuth mAuth;
    GeoQuery geoQuery;
    double locationLat1,locationLat2;
    double locationLng1, locationLng2;
    public Location Loc1;
    private int intRadius;

    private String radius;
    private String CurrentUserId, categoryID;

    private DatabaseReference usersDb, mUserDatabase, userLocationRef, closeLocationRef, userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_category);

        //ID kliknietej kategorii
        categoryID = getIntent().getExtras().getString("CategoryId");

        //Odwołanie do Auth bazy
        mAuth = FirebaseAuth.getInstance();

        //ID obecnego Uzytkownika
        CurrentUserId = mAuth.getCurrentUser().getUid();

        //DatabaseReference
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId);

        userLocationRef = FirebaseDatabase.getInstance().getReference().child("location").child(CurrentUserId).child("l");


        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        userLocation = FirebaseDatabase.getInstance().getReference().child("location");

        //ID kliknietej kategorii
        categoryID = getIntent().getExtras().getString("CategoryId");

        //Ustawienie RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        //Podpiecie LayoutMenagera
        mUsersInCategoryLayoutMenager = new LinearLayoutManager(UsersInCategoryActivity.this);
        mRecyclerView.setLayoutManager(mUsersInCategoryLayoutMenager);
        mUsersInCategoryAdapter = new UsersInCategoryAdapter(getDataSetUsersInCategory(), UsersInCategoryActivity.this);
        mRecyclerView.setAdapter(mUsersInCategoryAdapter);

        //Metoda pobiera promień. Start Activity
        getUserdRadius();
        }

        //Metoda pobiera promien uzytkownika
        public void getUserdRadius() {

            mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                        //Pobieranie promienia wyszukiwania
                        if (map.get("radius") != null) {

                            radius = map.get("radius").toString();
                            intRadius = Integer.parseInt(radius);

                            //metoda pobiera lokalizacje uzytkownika
                            getUserdLocation();

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        ////metoda pobiera lokalizacje uzytkownika
        public void getUserdLocation() {

            userLocationRef.addValueEventListener(new ValueEventListener() {
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
                        getClosestUsers();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
   private String formattedDistanceString;
        //Metoda pobiera uzytkownikow znajdujacych sie w promienu
        public void getClosestUsers() {

            GeoFire geoFire = new GeoFire(userLocation);

            //zapytanie i lokalizacja osoby szukajacej centru
            geoQuery = geoFire.queryAtLocation(new GeoLocation(locationLat1, locationLng1), intRadius);

            geoQuery.removeAllListeners();

            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, GeoLocation location) {
                    closeLocationRef = FirebaseDatabase.getInstance().getReference().child("location").child(key).child("l");
                    closeLocationRef.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                                //Pobranie lokalizacji uzytkownika w pobliz
                                List<Object> map = (List<Object>) dataSnapshot.getValue();
                                if (map.get(0) != null) {
                                    locationLat2 = Double.parseDouble(map.get(0).toString());

                                }
                                if (map.get(1) != null) {
                                    locationLng2 = Double.parseDouble(map.get(1).toString());
                                }
                            }

                            //Lokalizajcia uzytkownika w poblizu
                            Location Loc2 = new Location("");
                            Loc2.setLatitude(locationLat2);
                            Loc2.setLongitude(locationLng2);

                            float distance = Loc1.distanceTo(Loc2)/1000;
                            formattedDistanceString = String.format("%.1f", distance);
                           Log.e("tag", "formattedDistanceString :         " + formattedDistanceString + "     "+ Loc1 + "" +Loc2);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    FetchUsersInCategoryInformation(key,formattedDistanceString);

                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {



                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });
        }

        private void FetchUsersInCategoryInformation(final String key, final String Distance) {

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !dataSnapshot.child("follow").child("no").hasChild(CurrentUserId) ) {
                    if (!key.equals(FirebaseAuth.getInstance().getUid())) {

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
                        Log.e("tag", "Distance :         " + Distance );

                        //przypisanie do obiektu zmiennych
                        UsersInCategoryObject item = new UsersInCategoryObject(Id, Name, ImageUrl, Description, Brith, Sex, Phone,Distance,City);
                        resoultUsersInCategory.add(item);
                        mUsersInCategoryAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        }

        private List<UsersInCategoryObject> getDataSetUsersInCategory() {

            return resoultUsersInCategory;
        }
}

