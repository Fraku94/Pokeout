package com.example.pokeout.pokeout.CategoryDescryption;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;

public class CategoryDescryptionActivity extends AppCompatActivity {

    private TextView mName, mDescryption;
    private ImageView mImage;

    String ImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_descryption);


        //TextView
        mName = (TextView)findViewById(R.id.nameCategoryDescryption);
        mDescryption = (TextView)findViewById(R.id.descriptionCategoryDescryption);


        //ImageView
        mImage = (ImageView)findViewById(R.id.imageCategoryDescryption);

        //Pobranie danytch z bundle i przyupisanie wartosci do Textview i ImageView

        mName.setText(getIntent().getExtras().getString("Name"));
        mDescryption.setText(getIntent().getExtras().getString("Descryption"));

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView

        ImageUrl = getIntent().getExtras().getString("ImageUrl");
        if(!ImageUrl.equals("default")){
            Glide.with(getApplication()).load(ImageUrl).into(mImage);
        }

    }
}