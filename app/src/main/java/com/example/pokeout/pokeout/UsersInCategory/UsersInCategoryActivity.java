package com.example.pokeout.pokeout.UsersInCategory;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pokeout.pokeout.Connect.ConnectActivity;
import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UserDescryption.UserDescryptionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class UsersInCategoryActivity extends AppCompatActivity {

    private UsersInCategoryObject cards_data[];
    private UsersInCategoryAdapter UsersInCategoryAdapter;
    private int i;

    private FirebaseAuth mAuth;

    private String currentUId, categoryID;

    private DatabaseReference usersDb;


    ListView listView;
    List<UsersInCategoryObject> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_category);

        //ID kliknietej kategorii
        categoryID = getIntent().getExtras().getString("CategoryId");

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        getUserId();



        rowItems = new ArrayList<UsersInCategoryObject>();

        //przypsianie layoutu adapterowi
        UsersInCategoryAdapter = new UsersInCategoryAdapter(this, R.layout.item_users_in_category, rowItems );
        //biblioteka do przesówania kart
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(UsersInCategoryAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                UsersInCategoryAdapter.notifyDataSetChanged();


            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                UsersInCategoryObject obj = (UsersInCategoryObject) dataObject;
                String userId = obj.getId();
                //dodanie do bazy Firebase połaczenia Nie
                usersDb.child(userId).child("follow").child("no").child(currentUId).setValue(true);
                Toast.makeText(UsersInCategoryActivity.this, "Left", Toast.LENGTH_SHORT).show();
                if (rowItems.isEmpty()){
                    setContentView(R.layout.activity_users_in_category_empty);
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                UsersInCategoryObject obj = (UsersInCategoryObject) dataObject;
                String userId = obj.getId();
                //dodanie do bazy Firebase połaczenia Tak
                usersDb.child(userId).child("follow").child("yes").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                Toast.makeText(UsersInCategoryActivity.this, "Right", Toast.LENGTH_SHORT).show();
                if (rowItems.isEmpty()){
                    setContentView(R.layout.activity_users_in_category_empty);
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                UsersInCategoryObject obj = (UsersInCategoryObject) dataObject;
                Intent intent = new Intent(getApplicationContext(), UserDescryptionActivity.class);
                Bundle b = new Bundle();
                b.putString("Name", obj.getName());
                b.putString("ImageUrl", obj.getImageUrl());
                b.putString("Descryption", obj.getDescryption());
                b.putString("Brith", obj.getBrith());
                b.putString("Sex", obj.getSex());
                b.putString("Phone", obj.getPhone());
                intent.putExtras(b);
                startActivity(intent);

            }
        });



    }
    //Sprawdzanie czy uzytkownicy sie połączyli jesli tak to wyswietla sie toast
    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("follow").child("yes").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("follow").child("connect").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("follow").child("connect").child(dataSnapshot.getKey()).child("ChatId").setValue(key);

                    Toast.makeText(UsersInCategoryActivity.this, "Is Conenct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void getUserId(){

        final String adminId = "UYjTdbeg7zXSwXFhN72eDPseU1R2";
        DatabaseReference usersInCategorydb = FirebaseDatabase.getInstance().getReference().child("Category").child(categoryID).child("users");
        usersInCategorydb.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //Sprawdzenie czy istnieje
                if (dataSnapshot.exists()) {

                    //pobranie wartości z "category"
                    for (DataSnapshot userInCategory : dataSnapshot.getChildren()) {

                        if (!userInCategory.getKey().equals(adminId)) {
                            //Wywolanie metody zbierajacej informacje o kategorii z przekazaniem w niej ID danej kategorii
                            //getKey() pobiera ID kategorii
                            FetchUsersInCategoryInformation(userInCategory.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
    private void FetchUsersInCategoryInformation(final String key) {

        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !dataSnapshot.child("follow").child("no").hasChild(currentUId) && !dataSnapshot.child("follow").child("yes").hasChild(currentUId)) {
                    if (!key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        String Id = dataSnapshot.getKey();
                        String Name = "";
                        String ImageUrl = "";
                        String Description = "";
                        String Brith = "";
                        String Sex = "";
                        String Phone = "";
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
                        //przypisanie do obiektu Cards
                        UsersInCategoryObject item = new UsersInCategoryObject(Id, Name, ImageUrl, Description, Brith, Sex, Phone);
                        rowItems.add(item);
                        UsersInCategoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void goToConnect(View view) {

        Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
        startActivity(intent);
        return;
    }
}

