package com.example.pokeout.pokeout.UserDescryption;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDescryptionActivity extends AppCompatActivity {


    private RecyclerView.Adapter mUserDescryptionAdapter;
    private RecyclerView.LayoutManager mUserDescryptionLayoutMenager;

    private ArrayList<UserDescryptionObject> resoultUserDescryption = new ArrayList<UserDescryptionObject>();

    private TextView mName, mDescryption, mBrith, mSex, mPhone;
    private ImageView mImage;

    private String ImageUrl, mUserId;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mUDescryptionAdapter;
    private RecyclerView.LayoutManager mUDescryptionLayoutMenager;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_descryption);

        //TextView
        mName = (TextView)findViewById(R.id.nameUserDescryption);
        mDescryption = (TextView)findViewById(R.id.descriptionUserDescryption);
        mBrith = (TextView)findViewById(R.id.brithUserDescryption);
        mSex = (TextView)findViewById(R.id.sexUserDescryption);
        mPhone = (TextView)findViewById(R.id.phoneUserDescryption);
        //mCity = (TextView)findViewById(R.id.cityUserDescryption);

        //ImageView
        mImage = (ImageView)findViewById(R.id.imageUserDescryption);

        //Pobranie danytch z bundle i przyupisanie wartosci do Textview i ImageView

        mUserId = getIntent().getExtras().getString("Id");
        mName.setText(getIntent().getExtras().getString("Name"));
        mDescryption.setText(getIntent().getExtras().getString("Descryption"));
        mBrith.setText(getIntent().getExtras().getString("Brith"));
        mSex.setText(getIntent().getExtras().getString("Sex"));
        mPhone.setText(getIntent().getExtras().getString("Phone"));
      //  mCity.setText(getIntent().getExtras().getString("City"));

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView

        ImageUrl = getIntent().getExtras().getString("ImageUrl");
        if(!ImageUrl.equals("default")){
            Glide.with(getApplication()).load(ImageUrl).into(mImage);
        }

        //Ustawienie RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        //Podpiecie LayoutMenagera
        mUserDescryptionLayoutMenager = new LinearLayoutManager(UserDescryptionActivity.this);
        mRecyclerView.setLayoutManager(mUserDescryptionLayoutMenager);
        mUserDescryptionAdapter = new UserDescryptionAdapter(getDataSetUserDescryption(),UserDescryptionActivity.this);
        mRecyclerView.setAdapter(mUserDescryptionAdapter);
        Log.e("tttttt","mmmmm  "+mUserId);
    }

    private List<UserDescryptionObject> getDataSetUserDescryption() {

        //Tu Startuje fragment
        getUserId();

        return resoultUserDescryption;

    }
    private int i = 0;
    private void getUserId() {

        //Referencja do bazy Category
        DatabaseReference bestdb = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId).child("category");
        bestdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Sprawdzenie czy istnieje
                if (dataSnapshot.exists()) {

                    //pobranie wartości z "category"
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        Log.e("tttttt","mmmmm  "+user.getKey());
                        //Wywolanie metody zbierajacej informacje o kategorii z przekazaniem w niej ID danej kategorii
                        //getKey() pobiera ID kategorii
                        if (i<5){
                            FetchCategoryInformation(user.getKey());
                            i++;
                        }

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void FetchCategoryInformation( final String key) {

        //Referencja do bazy Category>>categoryID. przekazanie zmiennyej categoryID z getCategoryId(); w metodzie jako key
        DatabaseReference categoryDb = FirebaseDatabase.getInstance().getReference().child("Category").child(key);
        categoryDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Sprawdzenie czy istnieje
                if (dataSnapshot.exists()) {

                    //Przypisanie wartosci zmiennym
                    //getKey() pobiera ID kategorii
                    String categoryId = dataSnapshot.getKey();
                    String name = "";
                    String categoryImageUrl = "";

                    //Pobranie warosci name jesli nie jest pusta i przypisanie do zmiennej
                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }

                    //Pobranie warosci linku zdjecia jesli nie jest pusty i przypisanie do zmiennej

                    if (dataSnapshot.child("categoryImageUrl").getValue() != null) {
                        categoryImageUrl = dataSnapshot.child("categoryImageUrl").getValue().toString();
                    }

                        //Dodanie zmeinnych do Obiektu (nazwy musza byc takie same jak w Objekcie
                    UserDescryptionObject object = new UserDescryptionObject(categoryId, name, categoryImageUrl);

                        //Metoda dodawania do Objektu
                        resoultUserDescryption.add(object);

                        //Metoda notujaca zmiany (Wywoluje zapisanie zmiennych)
                        mUserDescryptionAdapter.notifyDataSetChanged();
                    }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}