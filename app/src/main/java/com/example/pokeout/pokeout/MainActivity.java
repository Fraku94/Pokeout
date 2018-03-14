package com.example.pokeout.pokeout;



import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;


import com.example.pokeout.pokeout.Adapter.SampleFragmentPagerAdapter;
import com.example.pokeout.pokeout.LoginRegister.LoginActivity;
import com.example.pokeout.pokeout.Profil.ProfilActivity;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;

import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class MainActivity extends AppCompatActivity implements LocationListener  {


    private FusedLocationProviderClient mFusedLocationClient;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private ImageButton logout;
    double latitude, longitude;
    TextView locationTxt;
    String userId;
    private FirebaseAuth mAuth;
    private LatLng pickupLocation;
    String city;
    private DatabaseReference mUserDatabase;
private Location mLocation;
    LocationRequest mLocationRequest;


    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    public MainActivity() {
    }

//ffsf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = getFusedLocationProviderClient(this);

        mAuth = FirebaseAuth.getInstance();
        //Wywoolanie obiektu  buttona do layotu do wylogowania
        logout = (ImageButton) findViewById(R.id.menu_logout);
        locationTxt = (TextView) findViewById(R.id.locationTxt);
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
        getLastLocation();
        startLocationUpdates();






    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
mLocation=location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        pickupLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        Geocoder geocoder;
        List<Address> addresses;
//        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        if (location != null) {
//            mLocation = location;

            try {
                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                     city = addresses.get(0).getLocality();
                    locationTxt.setText(mLocation.getLatitude() + " " + city + " " + mLocation.getLongitude());
                    mAuth = FirebaseAuth.getInstance();

                    //Przypisanie id Uzytkownika
                    userId = mAuth.getCurrentUser().getUid();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("location");

                    GeoFire geoFire = new GeoFire(ref);


                    geoFire.setLocation(userId,new GeoLocation(mLocation.getLatitude(),mLocation.getLongitude()));
                    saveUserCity();
                }
            }catch (IOException e) {
                e.printStackTrace();
    }
        }
    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void saveUserCity() {

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        Map userInfo = new HashMap();
        userInfo.put("city", city);
        mUserDatabase.updateChildren(userInfo);

    }
    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }






    public void Logout(View view) {

            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();


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
                Toast.makeText(getApplicationContext(), "You clicked profile"+latitude, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    }












