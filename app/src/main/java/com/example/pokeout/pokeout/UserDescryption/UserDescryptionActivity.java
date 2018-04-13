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


    private TextView mName, mDescryption, mBrith, mSex, mPhone;
    private ImageView mImage;

    private String ImageUrl, mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_descryption);

        //TextView
        mName = findViewById(R.id.nameUserDescryption);
        mDescryption = findViewById(R.id.descriptionUserDescryption);
        mBrith = findViewById(R.id.brithUserDescryption);
        mSex = findViewById(R.id.sexUserDescryption);
        mPhone = findViewById(R.id.phoneUserDescryption);
        //mCity = (TextView)findViewById(R.id.cityUserDescryption);

        //ImageView
        mImage = findViewById(R.id.imageUserDescryption);

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

    }
}