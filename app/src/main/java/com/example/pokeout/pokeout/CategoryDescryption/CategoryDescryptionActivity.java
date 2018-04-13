package com.example.pokeout.pokeout.CategoryDescryption;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class CategoryDescryptionActivity extends AppCompatActivity {

    private TextView mName, mDescryption;
    private ImageView mImage;
    private String ImageUrl, CategoryID;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mCategoryDescryptionAdapter;
    private RecyclerView.LayoutManager mCategoryDescryptionLayoutMenager;

    private ArrayList<CategoryDescryptionObject> resoultCategoryDescryption = new ArrayList<CategoryDescryptionObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_descryption);


        //TextView
        mName = findViewById(R.id.nameCategoryDescryption);
        mDescryption = findViewById(R.id.descriptionCategoryDescryption);


        //ImageView
        mImage = findViewById(R.id.imageCategoryDescryption);

        //Pobranie danytch z bundle i przyupisanie wartosci do Textview i ImageView

        mName.setText(getIntent().getExtras().getString("Name"));
        mDescryption.setText(getIntent().getExtras().getString("Descryption"));

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView

        ImageUrl = getIntent().getExtras().getString("ImageUrl");
        if(!ImageUrl.equals("default")){
            Glide.with(getApplication()).load(ImageUrl).into(mImage);
        }

        CategoryID  = getIntent().getExtras().getString("Id");

        //Ustawienie RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        //Podpiecie LayoutMenagera
        mCategoryDescryptionLayoutMenager = new LinearLayoutManager(CategoryDescryptionActivity.this);
        mRecyclerView.setLayoutManager(mCategoryDescryptionLayoutMenager);
        mCategoryDescryptionAdapter = new CategoryDescryptionAdapter(getDataSetCategoryDescryption(),CategoryDescryptionActivity.this);
        mRecyclerView.setAdapter(mCategoryDescryptionAdapter);

    }

    private List<CategoryDescryptionObject> getDataSetCategoryDescryption() {

        //Tu Startuje fragment
        getCategoryId();

        return resoultCategoryDescryption;

    }
    private int i = 0;
    private void getCategoryId() {

        //Referencja do bazy Category
        DatabaseReference Categorydb = FirebaseDatabase.getInstance().getReference().child("Category").child(CategoryID).child("users");
        Categorydb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Sprawdzenie czy istnieje
                if (dataSnapshot.exists()) {

                    //pobranie wartości z "category"
                    for (DataSnapshot category : dataSnapshot.getChildren()) {

                        //Wywolanie metody zbierajacej informacje o kategorii z przekazaniem w niej ID danej kategorii
                        //getKey() pobiera ID kategorii
                        if (i<5){
                            FetchBestInformation(category.getKey());
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


    private void FetchBestInformation( final String key) {

        //Referencja do bazy Category>>categoryID. przekazanie zmiennyej categoryID z getCategoryId(); w metodzie jako key
        DatabaseReference categoryDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
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
                    if (dataSnapshot.child("profileImageUrl").getValue() != null) {
                        categoryImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }


                    //Dodanie zmeinnych do Obiektu (nazwy musza byc takie same jak w Objekcie
                    CategoryDescryptionObject object = new CategoryDescryptionObject(categoryId, name, categoryImageUrl);

                    //Metoda dodawania do Objektu
                    resoultCategoryDescryption.add(object);

                    //Metoda notujaca zmiany (Wywoluje zapisanie zmiennych)
                    mCategoryDescryptionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}