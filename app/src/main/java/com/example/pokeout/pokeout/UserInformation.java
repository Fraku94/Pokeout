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

public class UserInformation {


    public static ArrayList<String> listFollowingUsers = new ArrayList<>();
    public static ArrayList<String> listNotFollowingUsers = new ArrayList<>();
    public static ArrayList<String> listIsConnectUsers = new ArrayList<>();

    public void startFetching(){
        listFollowingUsers.clear();
        listNotFollowingUsers.clear();
        listIsConnectUsers.clear();
        getUserFollowing();
    }


    private void getUserFollowing() {

        DatabaseReference usersFolowingdb = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("follow").child("following").child("yes");
        usersFolowingdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists())
                {
                    String uid = dataSnapshot.getRef().getKey();
                    if (uid != null && !listFollowingUsers.contains(uid)){
                        listFollowingUsers.add(uid);
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String uid = dataSnapshot.getRef().getKey();
                    if (uid != null){
                        listFollowingUsers.remove(uid);
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
        DatabaseReference usersNotFolowingdb = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("follow").child("following").child("no");
        usersNotFolowingdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists())
                {
                    String uid = dataSnapshot.getRef().getKey();
                    if (uid != null && !listNotFollowingUsers.contains(uid)){
                        listNotFollowingUsers.add(uid);
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String uid = dataSnapshot.getRef().getKey();
                    if (uid != null){
                        listNotFollowingUsers.remove(uid);
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

        DatabaseReference usersIsConnectdb = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("follow").child("connect");
        usersIsConnectdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists())
                {
                    String uid = dataSnapshot.getRef().getKey();
                    if (uid != null && !listIsConnectUsers.contains(uid)){
                        listIsConnectUsers.add(uid);
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String uid = dataSnapshot.getRef().getKey();
                    if (uid != null){
                        listIsConnectUsers.remove(uid);
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
