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
        if (!ImageUrl.equals("default")) {
            Glide.with(getApplication()).load(ImageUrl).into(mImage);
        }

        CategoryID = getIntent().getExtras().getString("Id");

    }
}