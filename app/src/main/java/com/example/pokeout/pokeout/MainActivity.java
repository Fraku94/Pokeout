package com.example.pokeout.pokeout;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pokeout.pokeout.Adapter.SampleFragmentPagerAdapter;
import com.example.pokeout.pokeout.Profil.ProfilActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{

    private FirebaseAuth auth;
//ffsf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();


        // Wywoołanie ViePagera i ustawienie na nim PageAdaptera co pozwala na wyświetlanie treści
        //Znajduje viepager i pozwala uzytkownikowi na przesuwanie

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Tworzenie adaptera rozpoznajacego,który fragment ma się pojawić i usttawienie adatera w ViePagerze
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Znajdowanie Tablayout ,który pokaże napisy
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //Wyświetlanie Tablayout w ViePager
        //  1.aktualizuje gdy jest przysuwanie
        //  2.aktualizuje gdy jest wyswietlane
        //  3.Ustawia nazwy tablayout z viepager adapter
        tabLayout.setupWithViewPager(viewPager);


    }

    public void Logout(View view) {
        auth.signOut();
        Intent intent =new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void Profil(View view) {
        Intent intent =new Intent(MainActivity.this, ProfilActivity.class);
        startActivity(intent);
        return;
    }

    public void AddCategory(View view) {

        Intent intent =new Intent(MainActivity.this, CategoryAddActivity.class);
        startActivity(intent);
        return;

    }
}
