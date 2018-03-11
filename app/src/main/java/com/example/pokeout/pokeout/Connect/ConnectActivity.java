package com.example.pokeout.pokeout.Connect;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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

    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerViewConnect);
        mRecyclerview.setScrollbarFadingEnabled(false);
        mRecyclerview.setHasFixedSize(true);

        //ID obecnego Uzytkownika
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Przypisanie Layoutu
        mConnectLayoutMenager = new LinearLayoutManager(ConnectActivity.this);
        mRecyclerview.setLayoutManager(mConnectLayoutMenager);
        mConnectAdapter = new ConnectAdapter(getDataSetConnect(), ConnectActivity.this);
        mRecyclerview.setAdapter(mConnectAdapter);

       // Toast.makeText(ConnectActivity.this, "1", Toast.LENGTH_SHORT).show();
        getUserConnectId();

    }

    //pobieranie id uzytkownika z ktorym dalismy sobei TAK
    private void getUserConnectId() {

        DatabaseReference Connectdb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("follow").child("connect");


        Connectdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot connect : dataSnapshot.getChildren()) {
                        FetchConnectInformation(connect.getKey());


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    //Dodawanie informacji o uzytkowniku co dal nam rowniez tak
    private void FetchConnectInformation(String key) {

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userId = dataSnapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";

                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("profileImageUrl").getValue() != null) {
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    Log.e("TAG", userId);
                    Log.e("TAG", name);
                    Log.e("TAG", profileImageUrl);
                    ConnectObject obj = new ConnectObject(userId, name, profileImageUrl);
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