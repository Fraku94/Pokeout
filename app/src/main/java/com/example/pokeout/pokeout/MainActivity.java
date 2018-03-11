package com.example.pokeout.pokeout;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import com.example.pokeout.pokeout.Adapter.SampleFragmentPagerAdapter;
import com.example.pokeout.pokeout.LoginRegister.LoginActivity;
import com.example.pokeout.pokeout.Profil.ProfilActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private ImageButton logout;
//ffsf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        auth = FirebaseAuth.getInstance();
        //Wywoolanie obiektu  buttona do layotu do wylogowania
        logout =(ImageButton)findViewById(R.id.menu_logout);

        //Sprawdzenie obserwaowanych kategorii
        CategoryInformation categoryInformationListner = new CategoryInformation();
        categoryInformationListner.startFetching();

        //Sprawdzenie obserwaowanych uzytkownikow
        UserInformation userInformationListner = new UserInformation();
        userInformationListner.startFetching();


        // Wywołanie  obiektu toolbar i dolaczenie do layotu
        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Ustawienie toolbara jako action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

//    public void Profil(View view) {
//        Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
//        startActivity(intent);
//        return;
//    }

//    public void AddCategory(View view) {
//
//        Intent intent = new Intent(MainActivity.this, CategoryAddActivity.class);
//        startActivity(intent);
//        return;
//
//    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {


        // dodaje do menu itemy z layoutu i wyswietla je tylko inaczej
        // niz w ( onOptionsItemSelected) np:wywołanie dodatkowego menu,czy pokazaniepasek wuszukiwania
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);


        //getting the search view from the menu
        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        //getting search manager from systemservice
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //getting the search view
        final SearchView searchView = (SearchView) searchViewItem.getActionView();

        //Wyswietlenie podpowiedzi do wyszukiwania
        searchView.setQueryHint("Search Category...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //by setting it true we are making it iconified
        //so the search input will show up after taping the search iconified
        //if you want to make it visible all the time make it false
        searchView.setIconifiedByDefault(true);

        //here we will get the search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //logika wyszukiwania do dodania
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // akcje,ktore ma sie wykonac po kliknieciu na dany obiekt wskazany ID.Klikasz i odrazu wykonuje sie akcja np:odswiezanie,wylogowanie
            case R.id.menu_profile:
                Toast.makeText(this, "You clicked profile", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);
                break;
        }
            return super.onOptionsItemSelected(item);
        }
    }

