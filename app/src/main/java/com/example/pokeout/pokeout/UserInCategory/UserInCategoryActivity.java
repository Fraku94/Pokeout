package com.example.pokeout.pokeout.UserInCategory;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.pokeout.pokeout.R;
import com.example.pokeout.pokeout.UsersInCategory.UsersInCategoryViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInCategoryActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{


    private RecyclerView recyclerView;
    private List<UserInCategoryObject> UserInCategoryList;
    private UserInCategoryAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;

    private String currentUserId, categoryID;

    List<UserInCategoryObject> rowItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_in_category);


        //ID kliknietej kategorii
        categoryID = getIntent().getExtras().getString("CategoryId");

        //ID obecnego Uzytkownika
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        rowItems = new ArrayList<>();
        mAdapter = new UserInCategoryAdapter(this, rowItems);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(UserInCategoryActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new UserInCategoryAdapter(UserInCategoryActivity.this, getDataSetUserInCategory());
        recyclerView.setAdapter(mAdapter);
        getUsersId();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);



       ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
           @Override
           public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
               return false;
           }

           @Override
           public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

           }
           @Override
           public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
               super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
           }
       };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }



    private void getUsersId()
    {
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
        Log.e("tag", "pobieranie danych" + Name +"        " + Id);



                        UserInCategoryObject item = new UserInCategoryObject(Id, Name, ImageUrl, Description, Brith, Sex, Phone);
                        resoultUser.add(item);
                        mAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        ((UserInCategoryViewHolder) viewHolder).viewLeft.setBackgroundColor(0xE60000);
    if (viewHolder instanceof UserInCategoryViewHolder) {
        String name = resoultUser.get(viewHolder.getAdapterPosition()).getName();
        Log.e("TAG", "Name " + resoultUser.get(viewHolder.getAdapterPosition()).getName() + "   " + viewHolder.getAdapterPosition());
        // backup of removed item for undo purpose
        final UserInCategoryObject deletedItem = resoultUser.get(viewHolder.getAdapterPosition());
        final int deletedIndex = viewHolder.getAdapterPosition();

        mAdapter.removeItem(viewHolder.getAdapterPosition());

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar.make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
                mAdapter.restoreItem(deletedItem, deletedIndex);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    }


    private ArrayList<UserInCategoryObject> resoultUser = new ArrayList<UserInCategoryObject>();

    private List<UserInCategoryObject> getDataSetUserInCategory() {

        return resoultUser;

    }
}
