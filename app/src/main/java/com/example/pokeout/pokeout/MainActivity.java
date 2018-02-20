package com.example.pokeout.pokeout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
    }

    public void Logout(View view) {
        auth.signOut();
        Intent intent =new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
