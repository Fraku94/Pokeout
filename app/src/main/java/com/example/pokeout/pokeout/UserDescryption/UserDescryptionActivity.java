package com.example.pokeout.pokeout.UserDescryption;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;

import org.w3c.dom.Text;

public class UserDescryptionActivity extends AppCompatActivity {

    private TextView mName, mDescryption, mBrith, mSex, mPhone;
    private ImageView mImage;

    String ImageUrl;

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

        //ImageView
        mImage = (ImageView)findViewById(R.id.imageUserDescryption);

        //Pobranie danytch z bundle i przyupisanie wartosci do Textview i ImageView

        mName.setText(getIntent().getExtras().getString("Name"));
        mDescryption.setText(getIntent().getExtras().getString("Descryption"));
        mBrith.setText(getIntent().getExtras().getString("Brith"));
        mSex.setText(getIntent().getExtras().getString("Sex"));
        mPhone.setText(getIntent().getExtras().getString("Phone"));

        //Sprawdzenie czy wartosc linku to "default" jesli nie ma załadować link i podpiac zdjecie ImageView

        ImageUrl = getIntent().getExtras().getString("ImageUrl");
        if(!ImageUrl.equals("default")){
            Glide.with(getApplication()).load(ImageUrl).into(mImage);
        }

    }
}
