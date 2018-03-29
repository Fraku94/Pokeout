package com.example.pokeout.pokeout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pokeout.pokeout.Search.SearchAdapater;
import com.example.pokeout.pokeout.Search.SearchObject;
import com.example.pokeout.pokeout.UsersInCategory.UsersInCategoryAdapter;
import com.example.pokeout.pokeout.UsersInCategory.UsersInCategoryObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    EditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mInput = findViewById(R.id.input);
        Button mSearch = findViewById(R.id.search);

        mRecyclerView = findViewById(R.id.recylerview_search_item);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchAdapater(getDataSet(),getApplication());
        mRecyclerView.setAdapter(mAdapter);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                listenForData();
            }
        });

    }



    private void listenForData() {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference animalsRef = rootRef.child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean found;
                String uid = dataSnapshot.getRef().getKey();
                String Name;
                String search = mInput.getText().toString();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Name = ds.child("name").getKey();
                    found = Name.contains(search);
                    Log.e("TAG", Name + " / " + found);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };


//
//        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
//        Query query = usersDb.orderByChild("email").startAt(mInput.getText().toString()).endAt(mInput.getText().toString() + "\uf8ff");
//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String email = "";
//                String uid = dataSnapshot.getRef().getKey();
//                if(dataSnapshot.child("email").getValue() != null){
//                    email = dataSnapshot.child("email").getValue().toString();
//                }
//                if(!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
//                    SearchObject obj = new SearchObject(email, uid);
//                    results.add(obj);
//                    mAdapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
    private void clear() {
        int size = this.results.size();
        this.results.clear();
        mAdapter.notifyItemRangeChanged(0, size);
    }



    private ArrayList<SearchObject> results = new ArrayList<>();
    private ArrayList<SearchObject> getDataSet() {
        listenForData();
        return results;
    }
}
