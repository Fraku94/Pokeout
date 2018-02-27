package com.example.pokeout.pokeout.UsersInCategory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pokeout.pokeout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersInCategoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mUsersInCategoryAdapter;
    private RecyclerView.LayoutManager mUsersInCategoryLayoutMenager;
    private String currentUserId, categoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_category);

        //ID kliknietej kategorii
        categoryID = getIntent().getExtras().getString("CategoryId");

        //ID obecnego Uzytkownika
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mUsersInCategoryLayoutMenager = new LinearLayoutManager(UsersInCategoryActivity.this);
        mRecyclerView.setLayoutManager(mUsersInCategoryLayoutMenager);
        mUsersInCategoryAdapter = new UsersInCategoryAdapter(getDataSetUsersInCategory(), UsersInCategoryActivity.this);
        mRecyclerView.setAdapter(mUsersInCategoryAdapter);

        getUsersId();
    }

    private void getUsersId()
    {
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

                        //Wywolanie metody zbierajacej informacje o kategorii z przekazaniem w niej ID danej kategorii
                        //getKey() pobiera ID kategorii
                        FetchUsersInCategoryInformation(userInCategory.getKey());
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
                if (dataSnapshot.exists()) {
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
                        UsersInCategoryObject object = new UsersInCategoryObject(Id, Name, ImageUrl, Description, Brith, Sex, Phone);
                        resoultUsersInCategory.add(object);
                        mUsersInCategoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<UsersInCategoryObject> resoultUsersInCategory = new ArrayList<UsersInCategoryObject>();

    private List<UsersInCategoryObject> getDataSetUsersInCategory() {

        return resoultUsersInCategory;

    }
}
