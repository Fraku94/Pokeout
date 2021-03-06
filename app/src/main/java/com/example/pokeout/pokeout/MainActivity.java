package com.example.pokeout.pokeout;


import android.content.Intent;
import android.content.IntentSender;
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
import com.example.pokeout.pokeout.Adapter.ZoomOutPageTransformer;
import com.example.pokeout.pokeout.CategoryAdd.CategoryAddActivity;
import com.example.pokeout.pokeout.Connect.ConnectActivity;
import com.example.pokeout.pokeout.LoginRegister.LoginActivity;
import com.example.pokeout.pokeout.Profil.ProfilActivity;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class MainActivity extends AppCompatActivity implements LocationListener {


    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    private Toolbar toolbar;
    private FirebaseAuth auth;
    private ImageButton logout;
    double latitude, longitude;
    TextView locationTxt;
    String userId;
    private FirebaseAuth mAuth;
    private DatabaseReference UserDb;
    private LatLng pickupLocation;
    String city;

    private DatabaseReference mUserDatabase;
    private Query mCategory;
    private Location mLocation;
    LocationRequest mLocationRequest;
    public String name;
    private MaterialSearchView searchView;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    //    ProgressBar load;
    ViewPager viewPager;
    private static final int REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE = 33;
    private static final int REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE = 34;
    private static final int REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE = 35;
    public MainActivity() {
    }

//ffsf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mAuth = FirebaseAuth.getInstance();
        UserDb = FirebaseDatabase.getInstance().getReference().child("Users");
        //Wywoolanie obiektu  buttona do layotu do wylogowania
        logout = findViewById(R.id.menu_logout);

        //Sprawdzenie obserwaowanych kategorii
        CategoryInformation categoryInformationListner = new CategoryInformation();
        categoryInformationListner.startFetching();

        //Sprawdzenie obserwaowanych uzytkownikow
        UserInformation userInformationListner = new UserInformation();
        userInformationListner.startFetching();


        // Wywołanie  obiektu toolbar i dolaczenie do layotu
        // Attaching the layout to the toolbar object
        toolbar = findViewById(R.id.toolbar);
        // Ustawienie toolbara jako action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Wywoołanie ViePagera i ustawienie na nim PageAdaptera co pozwala na wyświetlanie treści
        //Znajduje viepager i pozwala uzytkownikowi na przesuwanie
        viewPager = findViewById(R.id.viewpager);

        //Tworzenie adaptera rozpoznajacego,który fragment ma się pojawić i usttawienie adatera w ViePagerze
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //Odwołanie do klasy Transformer (Przejscai miedzy fragmentami)
        ZoomOutPageTransformer transformer = new ZoomOutPageTransformer();
        viewPager.setPageTransformer(true, transformer);


        // Znajdowanie Tablayout ,który pokaże napisy
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        //Wyświetlanie Tablayout w ViePager
        //  1.aktualizuje gdy jest przysuwanie
        //  2.aktualizuje gdy jest wyswietlane
        //  3.Ustawia nazwy tablayout z viepager adapter
//        checkForLocationRequest();
//        checkForLocationSettings();
        tabLayout.setupWithViewPager(viewPager);
        getLastLocation();
        startLocationUpdates();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        if (!checkPermissions()) {
//            requestPermissions();
//        } else {
//            getLastLocation();
//            startLocationUpdates();
//        }
//    }
//Check for location settings. if the location disabled prompt an alert dialog to redirect user.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    Toast.makeText(this,
                            "Denaid",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


public void checkForLocationSettings() {
    try {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.addLocationRequest(mLocationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(MainActivity.this);

        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Setting is success...
                        Toast.makeText(MainActivity.this, "Enabled the Location successfully. Now you can press the buttons..", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException sie) {
                                    sie.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Toast.makeText(MainActivity.this, "Setting change is not available.Try in another device.", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

    @Override
    public void onLocationChanged(Location location) {
//        load.setVisibility(View.VISIBLE);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
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
//                    locationTxt.setText(mLocation.getLatitude() + " " + city + " " + mLocation.getLongitude());
                    mAuth = FirebaseAuth.getInstance();

                    //Przypisanie id Uzytkownika
                    userId = mAuth.getCurrentUser().getUid();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("location");

                    GeoFire geoFire = new GeoFire(ref);


                    geoFire.setLocation(userId, new GeoLocation(mLocation.getLatitude(), mLocation.getLongitude()));
                    saveUserCity();
//                    load.setVisibility(View.GONE);
                }
            } catch (IOException e) {
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

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
//                            startLocationUpdates();
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


        if (
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

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

        String CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String CurrentUserToken = FirebaseInstanceId.getInstance().getToken();

        UserDb.child(CurrentUserId).child("deviceToken").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



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



        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // akcje,ktore ma sie wykonac po kliknieciu na dany obiekt wskazany ID.Klikasz i odrazu wykonuje sie akcja np:odswiezanie,wylogowanie
            case R.id.menu_profile:
               

                Toast.makeText(getApplicationContext(), "You clicked profile", Toast.LENGTH_SHORT).show();
                Intent profil = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(profil);
                break;
            case R.id.menuSearch:
                Toast.makeText(getApplicationContext(), "You clicked chat", Toast.LENGTH_SHORT).show();
                Intent search = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(search);
                break;
            case R.id.add:
                Toast.makeText(getApplicationContext(), "You clicked add category", Toast.LENGTH_SHORT).show();
                Intent add = new Intent(MainActivity.this, CategoryAddActivity.class);
                startActivity(add);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    }












