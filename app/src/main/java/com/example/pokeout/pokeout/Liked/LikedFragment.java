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

        Context context = getActivity();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mLikedLayoutMenager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLikedLayoutMenager);
        mLikedAdapter = new LikedAdapter(getDataSetLiked(), context);
        mRecyclerView.setAdapter(mLikedAdapter);

        getCategoryId();

        return rootView;
    }

    private void getCategoryId() {
        DatabaseReference likeddb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("category");
        likeddb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot category : dataSnapshot.getChildren()) {
                        FetchMatchInformation(category.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchInformation(String key) {

        DatabaseReference categoryDb = FirebaseDatabase.getInstance().getReference().child("Category").child(key);
        categoryDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String categoryId = dataSnapshot.getKey();
                    String name = "";
                    String categoryImageUrl = "";
                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("categoryImageUrl").getValue() != null) {
                        categoryImageUrl = dataSnapshot.child("categoryImageUrl").getValue().toString();
                    }
                    LikedObject object = new LikedObject(categoryId, name, categoryImageUrl);
                    resoultLiked.add(object);
                    mLikedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<LikedObject> resoultLiked = new ArrayList<LikedObject>();

    private List<LikedObject> getDataSetLiked() {

        return resoultLiked;

    }

}