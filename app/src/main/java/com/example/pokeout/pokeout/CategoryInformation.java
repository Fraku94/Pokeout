package com.example.pokeout.pokeout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Z710 on 2018-02-27.
 */

public class CategoryInformation {


    public static ArrayList<String> listFollowingCategory = new ArrayList<>();

        public void startFetching(){
            listFollowingCategory.clear();
            getCategoryFollowing();
        }

        private void getCategoryFollowing() {

            DatabaseReference usersFolowingdb = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("category");
            usersFolowingdb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if (dataSnapshot.exists())
                    {
                        String catid = dataSnapshot.getRef().getKey();
                        if (catid != null && !listFollowingCategory.contains(catid)){
                            listFollowingCategory.add(catid);
                        }
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        String catid = dataSnapshot.getRef().getKey();
                        if (catid != null){
                            listFollowingCategory.remove(catid);
                        }
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
}
